package io.github.cafeteriaguild.advweaponry.blocks.logic

import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import java.util.*

object CraftingLogic {
    val recipes = listOf(
        BaseRecipe(listOf(Items.IRON_INGOT to 3, Items.STICK to 1), Items.IRON_AXE),
        BaseRecipe(listOf(Items.IRON_INGOT to 2, Items.STICK to 1), Items.IRON_SWORD),
        BaseRecipe(listOf(Items.IRON_INGOT to 8), Items.IRON_CHESTPLATE),
        BaseRecipe(listOf(Items.IRON_INGOT to 5), Items.IRON_HELMET),
        BaseRecipe(listOf(Items.IRON_INGOT to 7), Items.IRON_LEGGINGS),
        BaseRecipe(listOf(Items.IRON_INGOT to 4), Items.IRON_BOOTS)
    )

    /**
     * This methods' result return chunks of 12 recipes.
     */
    fun recipesForItems(craftingItems: List<ItemStack>): List<List<PossibleRecipe>> {
        val groupedItems = craftingItems.groupBy({ it.item }, { it })
        val countedItems = groupedItems.mapValues { it.value.sumBy { it.count } }

        return recipes.filter { it.craftingMaterials.all { (item, count) -> count >= countedItems[item] ?: 0 } }
            .map { PossibleRecipe(it, consumeCraftingItems(it, craftingItems), 0) }
            .chunked(12)
    }

    private fun consumeCraftingItems(recipe: BaseRecipe, craftingItems: List<ItemStack>): List<ItemStack> {
        val result = craftingItems.mapTo(ArrayList()) { it.copy() }

        for ((item, count) in recipe.craftingMaterials) {
            var remaining = count
            // Yes, that's a for loop with index. Why? because I need to replace values without CME.
            for (i in result.indices) {
                val stack = result[i]
                if (stack.item == item) {
                    if (stack.count <= remaining) {
                        remaining -= stack.count
                        result[i] = ItemStack.EMPTY
                    } else {
                        stack.decrement(remaining)
                        break
                    }
                }
            }
        }

        return result
    }
}