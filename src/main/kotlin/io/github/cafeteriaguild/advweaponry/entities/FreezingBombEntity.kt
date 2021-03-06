package io.github.cafeteriaguild.advweaponry.entities

import io.github.cafeteriaguild.advweaponry.AWEntities
import io.github.cafeteriaguild.advweaponry.AWItems
import io.github.cafeteriaguild.advweaponry.identifier
import io.netty.buffer.Unpooled
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.block.Blocks
import net.minecraft.block.SnowBlock
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.entity.mob.BlazeEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.thrown.ThrownItemEntity
import net.minecraft.item.Item
import net.minecraft.network.PacketByteBuf
import net.minecraft.particle.ItemStackParticleEffect
import net.minecraft.particle.ParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.world.World

class FreezingBombEntity : ThrownItemEntity {

    constructor(world: World, user: LivingEntity?) : super(AWEntities.freezingBombEntityType, user, world)
    constructor(entityType: EntityType<out ThrownItemEntity>, world: World) : super(entityType, world)

    override fun getDefaultItem(): Item = AWItems.freezingBomb

    @Environment(EnvType.CLIENT)
    private fun getParticleParameters(): ParticleEffect? {
        val itemStack = this.item
        return (if (itemStack.isEmpty) ParticleTypes.ITEM_SNOWBALL else ItemStackParticleEffect(
            ParticleTypes.ITEM,
            itemStack
        )) as ParticleEffect
    }

    @Environment(EnvType.CLIENT)
    override fun handleStatus(status: Byte) {
        if (status.toInt() == 3) {
            val particleEffect = getParticleParameters()
            for (i in 0..7) {
                world.addParticle(particleEffect, this.x, this.y, this.z, 0.0, 0.0, 0.0)
            }
        }
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        super.onEntityHit(entityHitResult)
        val entity = entityHitResult.entity
        val i = if (entity is BlazeEntity) 3 else 0
        entity.damage(DamageSource.thrownProjectile(this, this.owner), i.toFloat())
    }

    override fun onCollision(hitResult: HitResult?) {
        super.onCollision(hitResult)
        if (!world.isClient) {
            val hitPos = hitResult!!.pos
            val centerPos = BlockPos(hitPos.x.toInt(), hitPos.y.toInt(), hitPos.z.toInt())
            val box = Box(centerPos).expand(2.0)
            val affectedPos = mutableListOf<BlockPos>()
            for (x in box.minX.toInt() until box.maxX.toInt())
                for (y in box.minY.toInt() until box.maxY.toInt())
                    for (z in box.minZ.toInt() until box.maxZ.toInt()) {
                        val pos = BlockPos(x, y, z)
                        val snowLayer = Blocks.SNOW.defaultState
                        val blockState = world.getBlockState(pos)

                        if (blockState.block == Blocks.SNOW) {
                            val grow = blockState.get(SnowBlock.LAYERS) + SnowBlock.LAYERS.values.random()
                            if (grow in SnowBlock.LAYERS.values) {
                                world.setBlockState(pos, snowLayer.with(SnowBlock.LAYERS, grow))
                            } else {
                                world.setBlockState(pos, Blocks.SNOW_BLOCK.defaultState)
                            }
                        } else if (blockState.material.isReplaceable && snowLayer.canPlaceAt(world, pos)) {
                            world.setBlockState(pos, snowLayer.with(SnowBlock.LAYERS, SnowBlock.LAYERS.values.random()))
                        } else if (world.isWater(pos)) {
                            world.setBlockState(pos, Blocks.ICE.defaultState)
                        } else continue
                        affectedPos.add(pos)
                    }
            val buf = PacketByteBuf(Unpooled.buffer())
            buf.writeInt(affectedPos.size)
            affectedPos.forEach { buf.writeBlockPos(it) }
            if (owner is PlayerEntity)
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(owner as PlayerEntity, PARTICLE_PACKET_ID, buf)
            world.getEntities(LivingEntity::class.java, box) { !it.isSpectator }.forEach { entity ->
                entity.addStatusEffect(
                    StatusEffectInstance(
                        StatusEffects.SLOWNESS, 200, 3
                    )
                )
            }
            world.sendEntityStatus(this, 3.toByte())
            this.remove()
        }
    }

    companion object {
        val PARTICLE_PACKET_ID = identifier("freezing_bomb_particle")
    }
}