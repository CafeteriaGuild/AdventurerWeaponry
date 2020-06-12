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
        val mappedKinds = craftingItems.mapNotNull {
            (it to MaterialKinds.fromItem(it.item)).takeUnless { (_, k) -> k.isEmpty() }
        }

        TODO()
    }

    private fun matchMaterials(recipe: BaseRecipe, mappedKinds: List<Pair<ItemStack, List<MaterialKind>>>) {

    }
}