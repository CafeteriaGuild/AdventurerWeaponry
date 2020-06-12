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
    val earthStaff = AWStaffItem(Abilities.dirtWall, Item.Settings().group(Weaponry.mainGroup).maxDamage(400))

    fun init() {
        identifier("earth_staff").item(earthStaff)
    }
}