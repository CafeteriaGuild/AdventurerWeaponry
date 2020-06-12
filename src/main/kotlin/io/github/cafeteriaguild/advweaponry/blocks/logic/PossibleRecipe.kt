package io.github.cafeteriaguild.advweaponry.blocks.logic

import net.minecraft.item.ItemStack

class PossibleRecipe(val recipe: BaseRecipe, val remainingStacks: List<ItemStack>, val maxModCount: Int) {
    fun withModifiers(modifiers: List<ItemStack>, originalItem: ItemStack?) : PossibleResult {
        return PossibleResult(ItemStack(recipe.output), modifiers)
    }
}