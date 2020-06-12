package io.github.cafeteriaguild.advweaponry.blocks.logic

import net.minecraft.item.Item

class BaseRecipe(val craftingMaterials: List<Pair<Item, Int>>, val output: Item) {
    val kindsUsed: LinkedHashSet<Item> = craftingMaterials.mapTo(LinkedHashSet()) { it.first }
    val craftingMaterialsMap = craftingMaterials.toMap()
}