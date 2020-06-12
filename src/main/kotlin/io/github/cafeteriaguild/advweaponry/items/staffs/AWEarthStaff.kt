package io.github.cafeteriaguild.advweaponry.items.staffs

import io.github.cafeteriaguild.advweaponry.items.AWStaffItem
import net.minecraft.block.Blocks
import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.sound.SoundCategory
import net.minecraft.util.ActionResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction

class AWEarthStaff(settings: Settings) : AWStaffItem(settings) {
    override fun useOnBlock(context: ItemUsageContext?): ActionResult {
        val pos = context?.blockPos
        val world = context?.world
        val facing = context?.playerFacing
        val isCreative = context?.player?.isCreative == true
        val inventory = context?.player?.inventory
        val xRange = if (facing?.axis == Direction.Axis.Z) 1.0 else 0.0
        val zRange = if (facing?.axis == Direction.Axis.X) 1.0 else 0.0
        val dirtState = Blocks.DIRT.defaultState
        val box = Box(pos?.up()).expand(xRange, 0.0, zRange).stretch(0.0, 2.0, 0.0)
        var shouldPlaySound = false
        for (y in box.minY.toInt() until box.maxY.toInt())
            for (x in box.minX.toInt() until box.maxX.toInt())
                for (z in box.minZ.toInt() until box.maxZ.toInt()) {
                    val blockPos = BlockPos(x, y, z)
                    if (isCreative
                        || inventory?.containsAny(setOf(Blocks.DIRT.asItem())) == true
                        && world?.isAir(blockPos) == true
                        && world.canSetBlock(blockPos)
                    ) {
                        if (!isCreative)
                            inventory?.getSlotWithStack(ItemStack(Blocks.DIRT))?.also { slot -> inventory.getStack(slot).decrement(1) }
                        world?.setBlockState(blockPos, dirtState)
                        if (world?.isClient == true)
                            MinecraftClient.getInstance().particleManager.addBlockBreakParticles(blockPos, dirtState)
                        shouldPlaySound = true
                    }
                }
        if (shouldPlaySound) {
            val blockSoundGroup = Blocks.DIRT.getSoundGroup(dirtState)
            world?.playSound(
                pos!!.x.toDouble(),
                pos.y.toDouble(),
                pos.z.toDouble(),
                blockSoundGroup.placeSound,
                SoundCategory.BLOCKS,
                (blockSoundGroup.getVolume() + 1.0f) / 2.0f,
                blockSoundGroup.getPitch() * 0.8f,
                false
            )
        }
        return ActionResult.SUCCESS
    }
}