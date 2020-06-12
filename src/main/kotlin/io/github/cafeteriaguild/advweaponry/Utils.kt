package io.github.cafeteriaguild.advweaponry

import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WWidget
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

fun mcIdentifier(path: String) = Identifier("minecraft", path)

fun identifier(path: String) = Identifier("advweaponry", path)

inline fun identifier(path: String, block: Identifier.() -> Unit) = identifier(path).run(block)

fun Identifier.item(item: Item) = apply {
    Registry.register(Registry.ITEM, this, item)
}

fun WGridPanel.add(widget: WWidget, x: Double, y: Double) {
    add(widget, x.toInt(), y.toInt())
    widget.setLocation((x * 18).toInt(), (y * 18).toInt())
}