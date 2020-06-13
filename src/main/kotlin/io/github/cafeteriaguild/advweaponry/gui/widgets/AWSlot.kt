package io.github.cafeteriaguild.advweaponry.gui.widgets

import io.github.cottonmc.cotton.gui.ValidatedSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack

class AWSlot(inventory: Inventory, index: Int, x: Int, y: Int) : ValidatedSlot(inventory, index, x, y) {
    var takeListener: (() -> Unit)? = null
    override fun onTakeItem(player: PlayerEntity?, stack: ItemStack?): ItemStack {
        if (player != null)
            takeListener?.invoke()
        return super.onTakeItem(player, stack)
    }
}