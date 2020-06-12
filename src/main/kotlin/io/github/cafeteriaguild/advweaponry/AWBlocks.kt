package io.github.cafeteriaguild.advweaponry

import io.github.cafeteriaguild.advweaponry.blocks.AdvTableBlock
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Block
import net.minecraft.block.Material

object AWBlocks {
    val ADV_TABLE: Block = AdvTableBlock(FabricBlockSettings.of(Material.WOOD))

    fun init() {
        identifier("advtable").blockAndItem(ADV_TABLE)
    }
}