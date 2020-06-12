package io.github.cafeteriaguild.advweaponry

import io.github.cafeteriaguild.advweaponry.entities.FreezingBombEntity
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.util.registry.Registry

object AWEntities {
    val freezingBombEntityType = EntityType.Builder.create(
        EntityType.EntityFactory<FreezingBombEntity> { entity, world -> FreezingBombEntity(entity, world) }, SpawnGroup.MISC
    ).setDimensions(0.25F, 0.25F).maxTrackingRange(4).trackingTickInterval(10).build("freezing_bomb")
    fun init() {
        identifier("freezing_bomb") {
            Registry.register(Registry.ENTITY_TYPE, this, freezingBombEntityType)
        }
        EntityRendererRegistry.INSTANCE.register(freezingBombEntityType) { dispatcher, context -> FlyingItemEntityRenderer<FreezingBombEntity>(dispatcher, context.itemRenderer) }
    }
}