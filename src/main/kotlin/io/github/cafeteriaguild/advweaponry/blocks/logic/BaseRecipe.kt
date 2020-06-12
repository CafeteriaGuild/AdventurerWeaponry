package io.github.cafeteriaguild.advweaponry.blocks.logic

import io.github.cafeteriaguild.advweaponry.materials.MaterialKind
import net.minecraft.item.Item

class BaseRecipe(val craftingMaterials: List<Pair<MaterialKind, Int>>, val output: Item)