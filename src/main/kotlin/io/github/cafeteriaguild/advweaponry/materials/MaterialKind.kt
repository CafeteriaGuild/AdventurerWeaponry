package io.github.cafeteriaguild.advweaponry.materials

import net.minecraft.item.Item

class MaterialKind(val values: List<Pair<Item, Double>>) {
    constructor(vararg values: Pair<Item, Double>) : this(values.toList())
}

