package io.github.cafeteriaguild.advweaponry.gui

import io.github.cafeteriaguild.advweaponry.add
import io.github.cafeteriaguild.advweaponry.blockentities.AdvTableBlockEntity
import io.github.cafeteriaguild.advweaponry.blocks.AdvTableBlock
import io.github.cafeteriaguild.advweaponry.gui.widgets.AWResultItemSlot
import io.github.cafeteriaguild.advweaponry.gui.widgets.WSelectableItemSlot
import io.github.cafeteriaguild.advweaponry.identifier
import io.github.cafeteriaguild.advweaponry.inventory.AWInventory
import io.github.cafeteriaguild.advweaponry.mixin.AccessorSyncedGuiDescription
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
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class TableController(
    syncId: Int,
    playerInventory: PlayerInventory,
    world: World,
    pos: BlockPos,
    private val ctx: ScreenHandlerContext
) : SyncedGuiDescription(null, syncId, playerInventory, getBlockInventory(ctx), getBlockPropertyDelegate(ctx)) {
    var selectedSlot: Int = -1

    // MODIFIERS
    private val modifiersInv: AWInventory

    // INPUT/OUTPUT ITEM
    private val ioInv: AWInventory

    // POSSIBLE OUTPUTS
    private val possibleOutputsInv: AWInventory

    init {
        val blockEntity = (world.getBlockEntity(pos) as? AdvTableBlockEntity
            ?: throw IllegalStateException("BlockEntity is not AdvTableBlockEntity"))
        modifiersInv = blockEntity.modifiersInv
        ioInv = blockEntity.ioInv
        possibleOutputsInv = blockEntity.possibleOutputsInv

        val panel = WGridPanel()
        setRootPanel(panel)
        panel.setSize(150, 120)

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
            val itemWidget = WSelectableItemSlot(this, ctx, blockEntity.possibleOutputsInv, slot)
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

        // MATERIALS
        val materialsLabel = WLabel(TranslatableText("container.winged.advtable.materials"))
        materialsLabel.setSize(9 * 18, 11)
        panel.add(materialsLabel, 0.0, 4.4)
        val materialsWidgets = WItemSlot.of(blockInventory, 0, 9, 1)
        panel.add(materialsWidgets, 0, 5)

        panel.add(createPlayerInventoryPanel(), 0.0, 6.2)

        panel.validate(this)
    }

    @Suppress("CAST_NEVER_SUCCEEDS") // FUCK YOU FIGHT ME
    val accessor = this as AccessorSyncedGuiDescription

    override fun onSlotClick(slotNumber: Int, button: Int, action: SlotActionType, player: PlayerEntity): ItemStack? {
        if (action == SlotActionType.QUICK_MOVE) {
            println("player did $slotNumber $button")
            if (slotNumber >= slots.size) return ItemStack.EMPTY
            val slot = slots[slotNumber] ?: return ItemStack.EMPTY

            val invToString = when (slot.inventory) {
                blockInventory -> "blockInventory"
                playerInventory -> "playerInventory"
                modifiersInv -> "modifiersInv"
                ioInv -> "ioInv"
                possibleOutputsInv -> ""
                else -> "<unknown inventory>"
            }

            println("slot is $invToString")

            if (!slot.canTakeItems(player)) return ItemStack.EMPTY
            if (!slot.hasStack()) return ItemStack.EMPTY
            val toTransfer = slot.stack

            if (slotNumber == 0) {
                if (!accessor.callInsertItem(toTransfer, playerInventory, true, player)) {
                    return ItemStack.EMPTY
                }
                ctx.run { world, pos ->
                    val blockEntity = world.getBlockEntity(pos)
                    if (blockEntity is AdvTableBlockEntity) {
                        blockEntity.output.takeListener?.invoke()
                    }
                }
            } else if (slotNumber in 49..66) {
                if (!accessor.callInsertItem(toTransfer, playerInventory, true, player)) {
                    return ItemStack.EMPTY
                }
            } else if (slotNumber in 1..36) {
                if (!accessor.callInsertItem(toTransfer, blockInventory, false, player)) {
                    if (!accessor.callInsertItem(toTransfer, modifiersInv, false, player)) {
                        return ItemStack.EMPTY
                    }
                }
            }

            if (toTransfer.isEmpty) {
                slot.stack = ItemStack.EMPTY
            } else {
                slot.markDirty()
            }

            return toTransfer.copy()
        }
        return super.onSlotClick(slotNumber, button, action, player)
    }

    companion object {
        val SCREEN_ID = identifier("table_screen")
    }
}