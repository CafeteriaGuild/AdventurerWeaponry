package io.github.cafeteriaguild.advweaponry

import io.github.cafeteriaguild.advweaponry.blocks.AdvTableBlock
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Material

object AWBlocks {
    val advTable = AdvTableBlock(FabricBlockSettings.of(Material.WOOD))

    fun init() {
        identifier("advtable").blockAndItem(advTable)
    }
}