package io.github.cafeteriaguild.advweaponry

import io.github.cafeteriaguild.advweaponry.gui.TableController
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.Identifier

object Weaponry : ModInitializer {
    val AW_GROUP: ItemGroup = FabricItemGroupBuilder.create(identifier("advweaponry_group"))
        .icon { ItemStack(AWBlocks.ADV_TABLE) }
        .build()

    override fun onInitialize() {
        AWBlocks.init()
        AWItems.init()

        ContainerProviderRegistry.INSTANCE.registerFactory(
            TableController.SCREEN_ID
        ) { syncId: Int, _: Identifier?, player: PlayerEntity, buf: PacketByteBuf ->
            TableController(syncId, player.inventory, ScreenHandlerContext.create(player.world, buf.readBlockPos()))
        }
    }
}