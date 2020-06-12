package io.github.cafeteriaguild.advweaponry

import io.github.cafeteriaguild.advweaponry.entities.FreezingBombEntity
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.registry.Registry

object AWEntities {
    val freezingBombEntityType = FabricEntityTypeBuilder.create(SpawnGroup.MISC,
        EntityType.EntityFactory<FreezingBombEntity> { entity, world -> FreezingBombEntity(entity, world)  })
        .dimensions(EntityDimensions.fixed(0.25F, 0.25F)).trackable(4, 10)
        .build()
    fun init() {
        identifier("freezing_bomb") {
            Registry.register(Registry.ENTITY_TYPE, this, freezingBombEntityType)
        }
    }
}