package io.github.cafeteriaguild.advweaponry.gui.widgets

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.widget.WWidget
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.inventory.Inventory

class AWResultItemSlot(inventory: Inventory, index: Int, private val slot: AWSlot) : WWidget() {
    val backgroundPainter = BackgroundPainter.SLOT
    override fun paint(matrices: MatrixStack?, x: Int, y: Int, mouseX: Int, mouseY: Int) {
        backgroundPainter.paintBackground(x, y, this)
    }
}