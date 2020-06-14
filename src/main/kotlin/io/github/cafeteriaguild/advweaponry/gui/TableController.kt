package io.github.cafeteriaguild.advweaponry.gui

import io.github.cafeteriaguild.advweaponry.add
import io.github.cafeteriaguild.advweaponry.blockentities.AdvTableBlockEntity
import io.github.cafeteriaguild.advweaponry.blocks.AdvTableBlock
import io.github.cafeteriaguild.advweaponry.gui.widgets.AWResultItemSlot
import io.github.cafeteriaguild.advweaponry.gui.widgets.WSelectableItemSlot
import io.github.cafeteriaguild.advweaponry.identifier
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.*
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.PacketByteBuf
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText

class TableController(syncId: Int, playerInventory: PlayerInventory, private val screenHandlerContext: ScreenHandlerContext)
    : SyncedGuiDescription(syncId, playerInventory, getBlockInventory(screenHandlerContext), getBlockPropertyDelegate(screenHandlerContext)) {
    var selectedSlot: Int = -1
    init {
        val panel = WGridPanel()
        setRootPanel(panel)
        panel.setSize(150, 120)
        screenHandlerContext.run { world, pos ->
            val blockEntity = world.getBlockEntity(pos)
            if (blockEntity is AdvTableBlockEntity) {
                val nextButton = WButton(LiteralText("↑"))
                val previousButton = WButton(LiteralText("↓"))
                nextButton.setOnClick {
                    blockEntity.currentPage = (blockEntity.currentPage + 1).coerceAtMost(blockEntity.maxPages)
                    selectedSlot = -1
                    val buf = PacketByteBuf(Unpooled.buffer())
                    buf.writeBlockPos(pos)
                    buf.writeInt(-1)
                    ClientSidePacketRegistry.INSTANCE.sendToServer(AdvTableBlock.SYNC_SELECTED_SLOT, buf)
                }
                previousButton.setOnClick {
                    blockEntity.currentPage = (blockEntity.currentPage - 1).coerceAtMost(blockEntity.maxPages)
                    selectedSlot = -1
                    val buf = PacketByteBuf(Unpooled.buffer())
                    buf.writeBlockPos(pos)
                    buf.writeInt(-1)
                    ClientSidePacketRegistry.INSTANCE.sendToServer(AdvTableBlock.SYNC_SELECTED_SLOT, buf)
                }
                val pageText = WText(LiteralText((blockEntity.currentPage + 1).toString()))
                panel.add(pageText, 0.3, 1.8)
                panel.add(nextButton, -0.1, 0.0)
                panel.add(previousButton, -0.1, 2.9)
                selectedSlot = blockEntity.selectedSlot
                // CRAFTABLES
                var x = 1
                var y = 0
                (0 until 12).forEach { slot ->
                    val itemWidget = WSelectableItemSlot(this, screenHandlerContext, blockEntity.possibleOutputsInv, slot)
                    itemWidget.isModifiable = false
                    if (x >= 4) {
                        y++
                        x = 1
                    }
                    panel.add(itemWidget, x, y)
                    x++
                }

                // MODIFIERS
                val modifierWidgets = WItemSlot.of(blockEntity.modifiersInv, 0, 2, 4)
                panel.add(modifierWidgets, 4.5, 0.0)

                // INPUT
                val inputItemWidget = WItemSlot.of(blockEntity.ioInv, 0)
                panel.add(inputItemWidget, 7.0, 0.5)

                // OUTPUT
                addSlotPeer(blockEntity.output)
                val outputItemWidget = AWResultItemSlot(blockEntity.ioInv, 1, blockEntity.output)
                panel.add(outputItemWidget, 7.0, 2.5)
            }
        }

        // MATERIALS
        val materialsLabel = WLabel(TranslatableText("container.winged.advtable.materials"))
        materialsLabel.setSize(9*18, 11)
        panel.add(materialsLabel, 0.0, 4.4)
        val materialsWidgets = WItemSlot.of(blockInventory, 0, 9, 1)
        panel.add(materialsWidgets, 0, 5)

        panel.add(createPlayerInventoryPanel(), 0.0, 6.2)

        panel.validate(this)
    }

    override fun onSlotClick(slotNumber: Int, button: Int, action: SlotActionType?, player: PlayerEntity?): ItemStack {
        val result = super.onSlotClick(slotNumber, button, action, player)
        if (!result.isEmpty && slotNumber == 0 && action == SlotActionType.QUICK_MOVE) {
            screenHandlerContext.run { world, pos ->
                val blockEntity = world.getBlockEntity(pos)
                if (blockEntity is AdvTableBlockEntity) {
                    blockEntity.output.takeListener?.invoke()
                }
            }
        }
        return result
    }

    companion object {
        val SCREEN_ID = identifier("table_screen")
    }
}