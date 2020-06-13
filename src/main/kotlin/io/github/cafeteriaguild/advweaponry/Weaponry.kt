package io.github.cafeteriaguild.advweaponry

import com.mojang.serialization.Lifecycle
import io.github.cafeteriaguild.advweaponry.blockentities.AdvTableBlockEntity
import io.github.cafeteriaguild.advweaponry.blocks.AdvTableBlock
import io.github.cafeteriaguild.advweaponry.gui.TableController
import io.github.cafeteriaguild.advweaponry.items.modifiers.Modifier
import io.github.cafeteriaguild.advweaponry.items.modifiers.Modifiers
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.fabricmc.fabric.api.container.ContainerProviderRegistry
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.util.Identifier
import net.minecraft.util.registry.RegistryKey
import net.minecraft.util.registry.SimpleRegistry

object Weaponry : ModInitializer {
    val mainGroup: ItemGroup = FabricItemGroupBuilder.create(identifier("main"))
        .icon { ItemStack(AWBlocks.advTable) }
        .build()

    val modifierRegistry = SimpleRegistry<Modifier>(
        RegistryKey.ofRegistry(identifier("modifier")),
        Lifecycle.experimental()
    )

    override fun onInitialize() {
        AWBlocks.init()
        AWItems.init()
        AWEntities.init()
        Modifiers.init()

        ContainerProviderRegistry.INSTANCE.registerFactory(
            TableController.SCREEN_ID
        ) { syncId: Int, _: Identifier?, player: PlayerEntity, buf: PacketByteBuf ->
            TableController(syncId, player.inventory, ScreenHandlerContext.create(player.world, buf.readBlockPos()))
        }
        ServerSidePacketRegistry.INSTANCE.register(AdvTableBlock.SYNC_SELECTED_SLOT) { ctx, buf ->
            val pos = buf.readBlockPos()
            val slot = buf.readInt()
            ctx.taskQueue.execute {
                val blockEntity = ctx.player.world.getBlockEntity(pos)
                if (blockEntity is AdvTableBlockEntity) {
                    blockEntity.selectedSlot = slot
                }
            }
        }
    }
}