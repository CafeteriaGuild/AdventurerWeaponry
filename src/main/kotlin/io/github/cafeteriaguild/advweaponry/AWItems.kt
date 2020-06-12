package io.github.cafeteriaguild.advweaponry

import io.github.cafeteriaguild.advweaponry.items.AWStaffItem
import io.github.cafeteriaguild.advweaponry.items.abilities.Abilities
import net.minecraft.item.Item

/**
 * Tools, Weapons, Books, etc
 */
@Suppress("MemberVisibilityCanBePrivate")
object AWItems {
    // TODO Properly calculate the items's damage.
    val EARTH_STAFF = AWStaffItem(Abilities.DIRT_WALL, Item.Settings().group(Weaponry.AW_GROUP).maxDamage(400))

    fun init() {
        identifier("earth_staff").item(EARTH_STAFF)
    }
}