package io.github.cafeteriaguild.advweaponry.items.abilities

import io.github.cafeteriaguild.advweaponry.identifier
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.block.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.network.PacketByteBuf
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction

class DirtWallAbility : Ability() {
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        val pos = context.blockPos
        val world = context.world
        if (world?.isClient == true) return ActionResult.SUCCESS
        val facing = context.playerFacing
        val isCreative = context.player?.isCreative == true
        val inventory = context.player?.inventory
        val xRange = if (facing.axis == Direction.Axis.Z) 1.0 else 0.0
        val zRange = if (facing.axis == Direction.Axis.X) 1.0 else 0.0
        val dirtState = Blocks.DIRT.defaultState
        val box = Box(pos.up()).expand(xRange, 0.0, zRange).stretch(0.0, 2.0, 0.0)

        val affectedPositions = ArrayList<BlockPos>()

        for (y in box.minY.toInt() until box.maxY.toInt()) {
            for (x in box.minX.toInt() until box.maxX.toInt()) {
                for (z in box.minZ.toInt() until box.maxZ.toInt()) {
                    val blockPos = BlockPos(x, y, z)
                    if ((isCreative || inventory?.containsAny(setOf(Blocks.DIRT.asItem())) == true)
                        && world?.isAir(blockPos) == true
                        && world.canSetBlock(blockPos)
                    ) {
                        if (!isCreative) {
                            inventory?.getSlotWithStack(ItemStack(Blocks.DIRT))?.also {
                                inventory.getStack(it).decrement(1)
                            }
                        }
                        world.setBlockState(blockPos, dirtState)
                        affectedPositions += blockPos

                    }
                }
            }
        }
        if (affectedPositions.isNotEmpty()) {
            val buf = PacketByteBuf(Unpooled.buffer()).apply {
                writeBlockPos(pos)
                writeInt(affectedPositions.size)
                affectedPositions.forEach { writeBlockPos(it) }
            }
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(context.player, DIRT_WALL_PACKET, buf)
        }
        return ActionResult.SUCCESS
    }

    companion object {
        val DIRT_WALL_PACKET = identifier("dirt_wall_packet")
    }
}
