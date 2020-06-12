package io.github.cafeteriaguild.advweaponry.gui.widgets

import io.github.cottonmc.cotton.gui.client.BackgroundPainter
import io.github.cottonmc.cotton.gui.client.ScreenDrawing
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WItemSlot
import net.minecraft.inventory.Inventory

class WSelectableItemSlot(inventory: Inventory, index: Int) : WItemSlot(inventory, index, 1, 1, false) {

    init {
        backgroundPainter = SELECTABLE_SLOT_PAINTER
    }
    override fun onClick(x: Int, y: Int, button: Int) {
        if (parent is AWWGridPanel) {
            (parent as AWWGridPanel).selectedSlot = this
        }
        super.onClick(x, y, button)
    }

    companion object {
        val SELECTABLE_SLOT_PAINTER: BackgroundPainter = BackgroundPainter { left, top, panel ->
            if (panel !is WSelectableItemSlot) {
                ScreenDrawing.drawBeveledPanel(
                    left - 1,
                    top - 1,
                    panel.width + 2,
                    panel.height + 2,
                    -1207959552,
                    1275068416,
                    -1191182337
                )
            } else {
                for (x in 0 until panel.width / 18) {
                    for (y in 0 until panel.height / 18) {
                        val index = x + y * (panel.width / 18)
                        val lo = -1207959552
                        val bg = 1275068416
                        val hi = -1191182337
                        var sx: Int
                        var sy: Int
                        if (panel.isBigSlot) {
                            ScreenDrawing.drawBeveledPanel(x * 18 + left - 3, y * 18 + top - 3, 26, 26, lo, bg, hi)
                            if (panel.focusedSlot == index || panel.parent is AWWGridPanel && (panel.parent as AWWGridPanel).selectedSlot == panel) {
                                sx = x * 18 + left - 3
                                sy = y * 18 + top - 3
                                ScreenDrawing.coloredRect(sx, sy, 26, 1, -96)
                                ScreenDrawing.coloredRect(sx, sy + 1, 1, 25, -96)
                                ScreenDrawing.coloredRect(sx + 26 - 1, sy + 1, 1, 25, -96)
                                ScreenDrawing.coloredRect(sx + 1, sy + 26 - 1, 25, 1, -96)
                            }
                        } else {
                            ScreenDrawing.drawBeveledPanel(x * 18 + left, y * 18 + top, 18, 18, lo, bg, hi)
                            if (panel.focusedSlot == index || panel.parent is AWWGridPanel && (panel.parent as AWWGridPanel).selectedSlot == panel) {
                                sx = x * 18 + left
                                sy = y * 18 + top
                                ScreenDrawing.coloredRect(sx, sy, 18, 1, -96)
                                ScreenDrawing.coloredRect(sx, sy + 1, 1, 17, -96)
                                ScreenDrawing.coloredRect(sx + 18 - 1, sy + 1, 1, 17, -96)
                                ScreenDrawing.coloredRect(sx + 1, sy + 18 - 1, 17, 1, -96)
                            }
                        }
                    }
                }
            }
        }
    }
}