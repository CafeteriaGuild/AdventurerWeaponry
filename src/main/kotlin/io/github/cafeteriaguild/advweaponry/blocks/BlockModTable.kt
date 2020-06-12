package io.github.cafeteriaguild.advweaponry.blocks

import io.github.cafeteriaguild.advweaponry.gui.TableController
import io.github.cafeteriaguild.advweaponry.inventory.AWInventory
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.InventoryProvider
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.WorldAccess

class BlockModTable(settings: Settings) : Block(settings), InventoryProvider {
    override fun getInventory(state: BlockState?, world: WorldAccess?, pos: BlockPos?): SidedInventory =
        AWInventory(31) { _, _ -> true }

    override fun onUse(
        state: BlockState?,
        world: World?,
        pos: BlockPos?,
        player: PlayerEntity?,
        hand: Hand?,
        hit: BlockHitResult?
    ): ActionResult {
        if (world?.isClient == false) {
            ContainerProviderRegistry.INSTANCE.openContainer(
                TableController.SCREEN_ID,
                player
            ) { packetByteBuf -> packetByteBuf.writeBlockPos(pos) }
        }
        return ActionResult.SUCCESS
    }
}