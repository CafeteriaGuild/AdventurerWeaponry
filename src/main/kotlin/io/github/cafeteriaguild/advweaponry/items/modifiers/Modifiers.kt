package io.github.cafeteriaguild.advweaponry.items.modifiers

import io.github.cafeteriaguild.advweaponry.Weaponry
import io.github.cafeteriaguild.advweaponry.identifier
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object Modifiers {
    val drawTimeReducer = Modifier(1)

    fun init() {
        identifier("draw_time_reducer").modifier(drawTimeReducer)
    }

    private fun Identifier.modifier(modifier: Modifier) = apply {
        Registry.register(Weaponry.modifierRegistry, this, modifier)
    }
}