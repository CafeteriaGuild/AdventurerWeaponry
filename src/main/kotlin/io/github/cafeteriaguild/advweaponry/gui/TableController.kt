package io.github.cafeteriaguild.advweaponry.gui

import io.github.cafeteriaguild.advweaponry.add
import io.github.cafeteriaguild.advweaponry.blockentities.AdvTableBlockEntity
import io.github.cafeteriaguild.advweaponry.gui.widgets.AWResultItemSlot
import io.github.cafeteriaguild.advweaponry.gui.widgets.WSelectableItemSlot
import io.github.cafeteriaguild.advweaponry.identifier
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.screen.slot.SlotActionType

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

                // CRAFTABLES
                var x = 0
                var y = 0
                (0 until 12).forEach { slot ->
                    val itemWidget = WSelectableItemSlot(this, screenHandlerContext, blockEntity.possibleOutputsInv, slot)
                    itemWidget.isModifiable = false
                    if (x >= 3) {
                        y++
                        x = 0
                    }
                    panel.add(itemWidget, x, y)
                    x++
                }

                // MODIFIERS
                val modifierWidgets = WItemSlot.of(blockEntity.modifiersInv, 0, 2, 4)
                panel.add(modifierWidgets, 4, 0)

                // INPUT
                val inputItemWidget = WItemSlot.of(blockEntity.inputInv, 0)
                panel.add(inputItemWidget, 7.0, 0.5)

                // OUTPUT
                addSlotPeer(blockEntity.output)
                val outputItemWidget = AWResultItemSlot(blockInventory, 9, blockEntity.output)
                panel.add(outputItemWidget, 7.0, 2.5)
            }
        }

        // MATERIALS
        val materialsWidgets = WItemSlot.of(blockInventory, 0, 9, 1)
        panel.add(materialsWidgets, 0, 5)

        panel.add(createPlayerInventoryPanel(), 0, 6)

        panel.validate(this)
    }

    override fun onSlotClick(slotNumber: Int, button: Int, action: SlotActionType?, player: PlayerEntity?): ItemStack {
        if (slotNumber == 0 && action == SlotActionType.QUICK_MOVE) {
            screenHandlerContext.run { world, pos ->
                val blockEntity = world.getBlockEntity(pos)
                if (blockEntity is AdvTableBlockEntity) {
                    blockEntity.output.takeListener?.invoke()
                }
            }
        }
        return super.onSlotClick(slotNumber, button, action, player)
    }

    companion object {
        val SCREEN_ID = identifier("table_screen")

        var MATERIALS_SLOTS = 20 until 29
        var POSSIBLE_RECIPES_SLOTS = 0 until 12
    }

}