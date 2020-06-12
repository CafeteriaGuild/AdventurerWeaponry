package io.github.cafeteriaguild.advweaponry

import io.github.cafeteriaguild.advweaponry.gui.TableController
import io.github.cafeteriaguild.advweaponry.items.abilities.DirtWallAbility
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.minecraft.block.Blocks
import net.minecraft.client.MinecraftClient
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.Identifier

object WeaponryClient : ClientModInitializer {
    override fun onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(
            TableController.SCREEN_ID
        ) { syncId: Int, _: Identifier?, player: PlayerEntity, buf: PacketByteBuf ->
            CottonInventoryScreen(
                TableController(
                    syncId,
                    player.inventory,
                    ScreenHandlerContext.create(player.world, buf.readBlockPos())
                ), player
            )
        }

        ClientSidePacketRegistry.INSTANCE.register(DirtWallAbility.DIRT_WALL_PACKET) { ctx, buf ->
            val pos = buf.readBlockPos()
            val affectedPositions = List(buf.readInt()) { buf.readBlockPos() }
            ctx.taskQueue.execute {
                MinecraftClient.getInstance().particleManager.addBlockBreakParticles(pos, Blocks.DIRT.defaultState)
            }
        }
    }
}