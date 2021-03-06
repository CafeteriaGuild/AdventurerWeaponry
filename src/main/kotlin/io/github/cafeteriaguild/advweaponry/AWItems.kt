package io.github.cafeteriaguild.advweaponry

import io.github.cafeteriaguild.advweaponry.items.AWFreezingBombItem
import io.github.cafeteriaguild.advweaponry.items.AWHammerItem
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
    val iceStaff = AWStaffItem(Abilities.freezingBomb, Item.Settings().group(Weaponry.mainGroup).maxDamage(400))
    val windStaff = AWStaffItem(Abilities.arrowStorm, Item.Settings().group(Weaponry.mainGroup).maxDamage(400))
    val freezingBomb = AWFreezingBombItem(Item.Settings().group(Weaponry.mainGroup))
    val ironHammer = AWHammerItem(Item.Settings().group(Weaponry.mainGroup))

    fun init() {
        identifier("earth_staff").item(earthStaff)
        identifier("ice_staff").item(iceStaff)
        identifier("wind_staff").item(windStaff)
        identifier("freezing_bomb").item(freezingBomb)
        identifier("iron_hammer").item(ironHammer)
    }
}