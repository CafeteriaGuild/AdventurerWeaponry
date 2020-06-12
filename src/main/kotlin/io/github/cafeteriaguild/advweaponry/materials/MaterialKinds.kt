package io.github.cafeteriaguild.advweaponry.materials

import net.minecraft.item.Item
import net.minecraft.item.Items
import java.util.concurrent.ConcurrentHashMap

object MaterialKinds {
    val WOOD_HANDLE = MaterialKind(Items.STICK to 0.5).registering()
    val IRON = MaterialKind(Items.IRON_INGOT to 0.75).registering()

    val allMaterials = ArrayList<MaterialKind>()

    fun fromItem(item: Item): List<MaterialKind> {
        return allMaterials.filter { it.values.any { (i, _) -> i == item } }
    }

    fun isMaterial(item: Item) {
        allMaterials.any { it.values.any { (i, _) -> i == item }  }
    }

    private val lookup = ConcurrentHashMap<Item, List<MaterialKind>>()

    private fun MaterialKind.registering() = apply {
        allMaterials += this
    }
}