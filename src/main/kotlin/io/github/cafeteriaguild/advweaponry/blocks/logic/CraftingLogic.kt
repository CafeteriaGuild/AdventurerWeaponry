package io.github.cafeteriaguild.advweaponry.blocks.logic

import io.github.cafeteriaguild.advweaponry.materials.MaterialKind
import io.github.cafeteriaguild.advweaponry.materials.MaterialKinds
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

object CraftingLogic {
    val recipes = listOf(
        BaseRecipe(listOf(MaterialKinds.IRON to 3, MaterialKinds.WOOD_HANDLE to 1), Items.IRON_AXE),
        BaseRecipe(listOf(MaterialKinds.IRON to 2, MaterialKinds.WOOD_HANDLE to 1), Items.IRON_SWORD),
        BaseRecipe(listOf(MaterialKinds.IRON to 8), Items.IRON_CHESTPLATE),
        BaseRecipe(listOf(MaterialKinds.IRON to 5), Items.IRON_HELMET),
        BaseRecipe(listOf(MaterialKinds.IRON to 7), Items.IRON_LEGGINGS),
        BaseRecipe(listOf(MaterialKinds.IRON to 4), Items.IRON_BOOTS)
    )

    /**
     * This methods' result return chunks of 12 recipes.
     */
    fun recipesForItems(craftingItems: List<ItemStack>): List<List<PossibleRecipe>> {
        val craftingItemKinds = craftingItems.map { it to MaterialKinds.fromItem(it.item) }
        val countedKinds = craftingItemKinds
            .flatMap { (stack, list) -> list.map { k -> k to stack } }
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.sumBy { it.count } }

        recipes.filter { it.craftingMaterials.all { (kind, count) -> count >= countedKinds[kind] ?: 0 } }
            .map {
                PossibleRecipe(it, resultItemsForRecipe(it, craftingItemKinds))
            }

        TODO()
    }

    private fun resultItemsForRecipe(r: BaseRecipe, k: List<Pair<ItemStack, List<MaterialKind>>>): List<ItemStack> {
        val remaining = r.craftingMaterialsMap.toMutableMap()
        val result = ArrayList<ItemStack>()
        for ((stack, list) in k) {

        }

        return result
    }
}