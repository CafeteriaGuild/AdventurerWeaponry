package io.github.cafeteriaguild.advweaponry.items

import io.github.cafeteriaguild.advweaponry.rangeTo
import net.minecraft.block.BlockState
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.PickaxeItem
import net.minecraft.item.ToolMaterials
import net.minecraft.util.Hand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class AWHammerItem(settings: Item.Settings) : PickaxeItem(ToolMaterials.IRON, 1,1f, settings) {
    override fun postMine(
        stack: ItemStack?,
        world: World?,
        state: BlockState?,
        pos: BlockPos,
        miner: LivingEntity?
    ): Boolean {
        if (world?.isClient == true) return true
        val itemStack = miner?.getStackInHand(Hand.MAIN_HAND)
        if (itemStack?.item is AWHammerItem) {
            val box = when (Direction.getEntityFacingOrder(miner)[0]!!) {
                Direction.DOWN, Direction.UP -> Box(
                    (pos.x - 1).toDouble(),
                    pos.y.toDouble(),
                    (pos.z - 1).toDouble(),
                    (pos.x + 1).toDouble(),
                    pos.y.toDouble(),
                    (pos.z + 1).toDouble()
                )
                Direction.NORTH, Direction.SOUTH -> Box(
                    (pos.x - 1).toDouble(),
                    (pos.y - 1).toDouble(),
                    pos.z.toDouble(),
                    (pos.x + 1).toDouble(),
                    (pos.y + 1).toDouble(),
                    pos.z.toDouble()
                )
                Direction.WEST, Direction.EAST -> Box(
                    pos.x.toDouble(),
                    (pos.y - 1).toDouble(),
                    (pos.z - 1).toDouble(),
                    pos.x.toDouble(),
                    (pos.y + 1).toDouble(),
                    (pos.z + 1).toDouble()
                )
            }
            for (x in box.minX rangeTo box.maxX)
                for (y in box.minY rangeTo box.maxY)
                    for (z in box.minZ rangeTo box.maxZ) {
                        world?.breakBlock(BlockPos(x, y, z), true, miner)
                    }
        }
        return true
    }
}