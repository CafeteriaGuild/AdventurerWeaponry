package io.github.cafeteriaguild.advweaponry

import io.github.cafeteriaguild.advweaponry.entities.FreezingBombEntity
import io.github.cafeteriaguild.advweaponry.gui.TableController
import io.github.cafeteriaguild.advweaponry.items.abilities.DirtWallAbility
import io.github.cottonmc.cotton.gui.client.CottonInventoryScreen
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.minecraft.block.Blocks
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.entity.FlyingItemEntityRenderer
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier

object WeaponryClient : ClientModInitializer {
    override fun onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(
            TableController.SCREEN_ID
        ) { syncId: Int, _: Identifier?, player: PlayerEntity, buf: PacketByteBuf ->
            val pos = buf.readBlockPos()
            CottonInventoryScreen(
                TableController(
                    syncId,
                    player.inventory,
                    player.world,
                    pos,
                    ScreenHandlerContext.create(player.world, pos)
                ), player
            )
        }

        ClientSidePacketRegistry.INSTANCE.register(DirtWallAbility.DIRT_WALL_PACKET) { ctx, buf ->
            val pos = buf.readBlockPos()
            val affectedPositions = List(buf.readInt()) { buf.readBlockPos() }
            ctx.taskQueue.execute {
                MinecraftClient.getInstance().soundManager.play(
                    PositionedSoundInstance(SoundEvents.BLOCK_GRAVEL_PLACE, SoundCategory.BLOCKS, 1f, 0.8f, pos)
                )
                affectedPositions.forEach {
                    MinecraftClient.getInstance().particleManager.addBlockBreakParticles(it, Blocks.DIRT.defaultState)
                }
            }
        }

        ClientSidePacketRegistry.INSTANCE.register(FreezingBombEntity.PARTICLE_PACKET_ID) { ctx, buf ->
            val affectedPositions = List(buf.readInt()) { buf.readBlockPos() }
            ctx.taskQueue.execute {
                affectedPositions.forEach {
                    MinecraftClient.getInstance().particleManager.addBlockBreakParticles(it, Blocks.SNOW.defaultState)
                }
            }
        }

        EntityRendererRegistry.INSTANCE.register(AWEntities.freezingBombEntityType) { dispatcher, context ->
            FlyingItemEntityRenderer<FreezingBombEntity>(dispatcher, context.itemRenderer)
        }
    }
}