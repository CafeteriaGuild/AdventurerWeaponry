package io.github.cafeteriaguild.advweaponry.gui

import io.github.cafeteriaguild.advweaponry.add
import io.github.cafeteriaguild.advweaponry.gui.widgets.AWWGridPanel
import io.github.cafeteriaguild.advweaponry.gui.widgets.WSelectableItemSlot
import io.github.cafeteriaguild.advweaponry.identifier
import io.github.cottonmc.cotton.gui.SyncedGuiDescription
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import io.github.cottonmc.cotton.gui.widget.WLabel
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandlerContext
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText

class TableController(syncId: Int, playerInventory: PlayerInventory, screenHandlerContext: ScreenHandlerContext)
    : SyncedGuiDescription(syncId, playerInventory, getBlockInventory(screenHandlerContext), getBlockPropertyDelegate(screenHandlerContext)) {
    init {
        val panel = AWWGridPanel()
        setRootPanel(panel)
        panel.setSize(150, 170)

        // CRAFTABLE SLOTS
        var x = 0
        var y = 0
        (0 until 12).forEach { slot ->
            val itemWidget = WSelectableItemSlot(blockInventory, slot)
            itemWidget.isModifiable = false
            if (x >= 3) {
                y++
                x = 0
            }
            panel.add(itemWidget, x, y)
            x++
        }
        // MODIFIERS SLOTS
        val modifierWidgets = WItemSlot.of(blockInventory, 12, 2, 4)
        panel.add(modifierWidgets, 4, 0)
        // MATERIALS SLOTS
        val materialsLabel = WLabel(TranslatableText("container.winged.advtable.materials"))
        materialsLabel.setSize(9*18, 11);
        panel.add(materialsLabel, 0.0, 4.4)
        val materialsWidgets = WItemSlot.of(blockInventory, 20, 9, 1)

        panel.add(materialsWidgets, 0, 5)
        val inputItemWidget = WItemSlot.of(blockInventory, 29)
        panel.add(inputItemWidget, 7.0, 0.5)
        val outputItemWidget = WItemSlot.outputOf(blockInventory, 30)
        panel.add(outputItemWidget, 7.0, 2.5)

        panel.add(createPlayerInventoryPanel(), 0.0, 6.2)

        panel.validate(this)
    }

    companion object {
        val SCREEN_ID = identifier("table_screen")
    }
}