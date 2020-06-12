package io.github.cafeteriaguild.advweaponry.gui

import io.github.cafeteriaguild.advweaponry.add
import io.github.cafeteriaguild.advweaponry.gui.widgets.AWWGridPanel
import io.github.cafeteriaguild.advweaponry.gui.widgets.WSelectableItemSlot
import io.github.cafeteriaguild.advweaponry.identifier
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext

class TableController(syncId: Int, playerInventory: PlayerInventory, screenHandlerContext: ScreenHandlerContext)
    : SyncedGuiDescription(syncId, playerInventory, getBlockInventory(screenHandlerContext), getBlockPropertyDelegate(screenHandlerContext)) {
    init {
        val panel = AWWGridPanel()
        setRootPanel(panel)
        panel.setSize(150, 120)

        // CRAFTABLE SLOTS
        var x = 0
        var y = 0
        (0 until 12).forEach { slot ->
            val itemWidget = WSelectableItemSlot(blockInventory, slot)
            if (x >= 3) {
                y++
                x = 0
            }
            panel.add(itemWidget, x, y)
            x++
        }
        // MODIFIERS SLOTS
        x = 4
        y = 0
        (12 until 20).forEach { slot ->
            val itemWidget = WItemSlot.of(blockInventory, slot)
            if (x >= 6) {
                y++
                x = 4
            }
            panel.add(itemWidget, x, y)
            x++
        }
        // MATERIALS SLOTS
        (20 until 29).forEach { slot ->
            val itemWidget = WItemSlot.of(blockInventory, slot)
            panel.add(itemWidget, slot - 20, 5)
        }
        val inputItemWidget = WItemSlot.of(blockInventory, 29)
        panel.add(inputItemWidget, 7.0, 0.5)
        val outputItemWidget = WItemSlot.outputOf(blockInventory, 30)
        panel.add(outputItemWidget, 7.0, 2.5)

        panel.add(createPlayerInventoryPanel(), 0, 6)

        panel.validate(this)
    }

    companion object {
        val SCREEN_ID = identifier("table_screen")
    }
}