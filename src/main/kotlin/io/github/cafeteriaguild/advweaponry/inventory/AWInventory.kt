package io.github.cafeteriaguild.advweaponry.inventory

import net.minecraft.inventory.SidedInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.math.Direction

class AWInventory(size: Int, val slotPredicate: (Int, ItemStack?) -> Boolean) : SimpleInventory(size), SidedInventory {
    override fun canExtract(slot: Int, stack: ItemStack?, dir: Direction?): Boolean = false

    override fun canInsert(slot: Int, stack: ItemStack?, dir: Direction?): Boolean = false

    override fun getAvailableSlots(side: Direction?): IntArray = IntArray(size()) { index -> index }

    override fun isValid(slot: Int, stack: ItemStack?): Boolean = slotPredicate(slot, stack)
}