package io.github.cafeteriaguild.advweaponry

import io.github.cafeteriaguild.advweaponry.blocks.BlockModTable
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.block.Block
import net.minecraft.block.Material
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.registry.Registry

object Weaponry : ModInitializer {
    override fun onInitialize() {
        identifier("table") {
            Registry.register(Registry.BLOCK, this, TABLE)
            Registry.register(Registry.ITEM, this, TABLE_BLOCK_ITEM)
        }
    }

    val MOD_GROUP: ItemGroup = FabricItemGroupBuilder.create(identifier("advweaponry_group")).icon { ItemStack(TABLE_BLOCK_ITEM) }.build()

    val TABLE: Block = BlockModTable(FabricBlockSettings.of(Material.WOOD))
    val TABLE_BLOCK_ITEM: BlockItem = BlockItem(TABLE, Item.Settings().group(MOD_GROUP))
}