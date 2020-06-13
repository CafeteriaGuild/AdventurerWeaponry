package io.github.cafeteriaguild.advweaponry.blocks

import io.github.cafeteriaguild.advweaponry.blockentities.AdvTableBlockEntity
import io.github.cafeteriaguild.advweaponry.gui.TableController
import io.github.cafeteriaguild.advweaponry.identifier
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.Block
import net.minecraft.block.BlockEntityProvider
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView
import net.minecraft.world.World

class AdvTableBlock(settings: Settings) : Block(settings), BlockEntityProvider {
    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand,
        hit: BlockHitResult
    ): ActionResult {
        if (!world.isClient) {
            ContainerProviderRegistry.INSTANCE.openContainer(TableController.SCREEN_ID, player) { buf ->
                buf.writeBlockPos(pos)
            }
        }
        return ActionResult.SUCCESS
    }

    override fun createBlockEntity(world: BlockView?): BlockEntity? = AdvTableBlockEntity()

    companion object {
        val SYNC_SELECTED_SLOT = identifier("sync_selected_slot")
    }
}