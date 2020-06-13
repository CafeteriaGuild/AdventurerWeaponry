package io.github.cafeteriaguild.advweaponry

import io.github.cafeteriaguild.advweaponry.blockentities.AdvTableBlockEntity
import io.github.cafeteriaguild.advweaponry.blocks.AdvTableBlock
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.Material
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.util.registry.Registry
import java.util.function.Supplier

object AWBlocks {
    val advTable = AdvTableBlock(FabricBlockSettings.of(Material.WOOD))
    val advTableBlockEntity = BlockEntityType.Builder.create(Supplier { AdvTableBlockEntity() }, advTable).build(null)

    fun init() {
        identifier("advtable") {
            blockAndItem(advTable)
            Registry.register(Registry.BLOCK_ENTITY_TYPE, this, advTableBlockEntity)
        }
    }
}