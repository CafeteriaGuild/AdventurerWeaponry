package io.github.cafeteriaguild.advweaponry

import io.github.cafeteriaguild.advweaponry.blocks.BlockModTable
import io.github.cafeteriaguild.advweaponry.gui.TableController
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object Weaponry : ModInitializer {
    override fun onInitialize() {
        identifier("table") {
            Registry.register(Registry.BLOCK, this, TABLE)
            Registry.register(Registry.ITEM, this, TABLE_BLOCK_ITEM)
        }

        ContainerProviderRegistry.INSTANCE.registerFactory(
            TableController.SCREEN_ID
        ) { syncId: Int, _: Identifier?, player: PlayerEntity, buf: PacketByteBuf ->
            TableController(syncId, player.inventory, ScreenHandlerContext.create(player.world, buf.readBlockPos()))
        }
    }

    val MOD_GROUP: ItemGroup = FabricItemGroupBuilder.create(identifier("advweaponry_group")).icon { ItemStack(TABLE_BLOCK_ITEM) }.build()

    val TABLE: Block = BlockModTable(FabricBlockSettings.of(Material.WOOD))
    val TABLE_BLOCK_ITEM: BlockItem = BlockItem(TABLE, Item.Settings().group(MOD_GROUP))
}