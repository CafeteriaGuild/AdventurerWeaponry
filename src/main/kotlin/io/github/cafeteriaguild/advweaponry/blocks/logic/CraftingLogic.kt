package io.github.cafeteriaguild.advweaponry.blocks.logic

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

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

        recipes.filter { it.craftingMaterials.all { (kind, count) -> count >= countedItems[kind] ?: 0 } }
            .map {
                PossibleRecipe(it, resultItemsForRecipe(it, groupedItems))
            }

        TODO()
    }

    private fun resultItemsForRecipe(r: BaseRecipe, k: Map<Item, List<ItemStack>>): List<ItemStack> {
        val remaining = r.craftingMaterialsMap.toMutableMap()
        val result = ArrayList<ItemStack>()
        for ((stack, list) in k) {

        }

        return result
    }
}