package io.github.cafeteriaguild.advweaponry.blocks.logic

import net.minecraft.item.ItemStack

class PossibleRecipe(val recipe: BaseRecipe, val remainingStacks: List<ItemStack>) {
    fun withModifiers(modifiers: List<ItemStack>, originalItem: ItemStack?) : PossibleResult {
        TODO()
    }
}