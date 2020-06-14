package io.github.cafeteriaguild.advweaponry.items.abilities

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ArrowItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.MathHelper
import net.minecraft.world.RayTraceContext
import net.minecraft.world.RayTraceContext.FluidHandling
import net.minecraft.world.World

class ArrowStormAbility : Ability() {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        val hitResult = rayTrace(world, user) ?: return TypedActionResult.pass(stack)
        if (hitResult.type == HitResult.Type.MISS) return TypedActionResult.pass(stack)
        val hitPos = hitResult.blockPos
        if (hitPos.isWithinDistance(user.pos, 20.0) && world.isSkyVisible(hitPos.up()) && stack.cooldown <= 0) {
            val xRange = (4 until 7).random() / 2
            val zRange = (4 until 7).random() / 2
            for (x in (hitPos.x - xRange)..(hitPos.x + xRange))
                for (z in (hitPos.z - zRange)..(hitPos.z + zRange)) {
                    for (arrows in 0..2) {
                        val arrowItem = Items.ARROW as ArrowItem
                        val arrowStack = ItemStack(arrowItem)
                        val arrowEntity = arrowItem.createArrow(world, arrowStack, user)
                        arrowEntity.setPos(hitPos.x.toDouble(), (hitPos.y + (70..130).random()).coerceAtMost(world.height).toDouble(), hitPos.z.toDouble())
                        arrowEntity.setProperties(user, 90f, 0f, 0f, 3.0f, 1.0f)
                        arrowEntity.isCritical = true
                        arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED
                        world.spawnEntity(arrowEntity)
                    }
                }
            stack.cooldown += 50
            return TypedActionResult.success(stack)
        }
        return super.use(world, user, hand)
    }

    private fun rayTrace(world: World, player: PlayerEntity): BlockHitResult? {
        val f = player.pitch
        val g = player.yaw
        val vec3d = player.getCameraPosVec(1.0f)
        val h = MathHelper.cos(-g * 0.017453292f - 3.1415927f)
        val i = MathHelper.sin(-g * 0.017453292f - 3.1415927f)
        val j = -MathHelper.cos(-f * 0.017453292f)
        val k = MathHelper.sin(-f * 0.017453292f)
        val l = i * j
        val n = h * j
        val vec3d2 = vec3d.add(l.toDouble() * 20.0, k.toDouble() * 20.0, n.toDouble() * 20.0)
        return world.rayTrace(RayTraceContext(vec3d, vec3d2, RayTraceContext.ShapeType.OUTLINE, FluidHandling.ANY, player))
    }
}