package io.github.cafeteriaguild.advweaponry

import io.github.cafeteriaguild.advweaponry.Weaponry.mainGroup
import io.github.cottonmc.cotton.gui.widget.WGridPanel
import io.github.cottonmc.cotton.gui.widget.WWidget
import net.minecraft.block.Block
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.MathHelper
import net.minecraft.util.registry.Registry
import net.minecraft.world.RayTraceContext
import net.minecraft.world.World

fun mcIdentifier(path: String) = Identifier("minecraft", path)

fun identifier(path: String) = Identifier("advweaponry", path)

inline fun identifier(path: String, block: Identifier.() -> Unit) = identifier(path).run(block)

fun Identifier.item(item: Item) = apply {
    Registry.register(Registry.ITEM, this, item)
}

fun Identifier.blockAndItem(block: Block, settings: Item.Settings = Item.Settings().group(mainGroup)) = apply {
    Registry.register(Registry.BLOCK, this, block)
    Registry.register(Registry.ITEM, this, BlockItem(block, settings))
}

fun WGridPanel.add(widget: WWidget, x: Double, y: Double) {
    add(widget, x.toInt(), y.toInt())
    widget.setLocation((x * 18).toInt(), (y * 18).toInt())
}

infix fun Double.rangeTo(double: Double) = toInt()..double.toInt()

private operator fun ClosedFloatingPointRange<Double>.iterator(): Iterator<Int> = this.iterator()


fun rayTrace(world: World, player: PlayerEntity, limit: Double): BlockHitResult? {
    val f = player.pitch
    val g = player.yaw
    val vec3d = player.getCameraPosVec(1.0f)
    val h = MathHelper.cos(-g * 0.017453292f - 3.1415927f)
    val i = MathHelper.sin(-g * 0.017453292f - 3.1415927f)
    val j = -MathHelper.cos(-f * 0.017453292f)
    val k = MathHelper.sin(-f * 0.017453292f)
    val l = i * j
    val n = h * j
    val vec3d2 = vec3d.add(l.toDouble() * limit, k.toDouble() * limit, n.toDouble() * limit)
    return world.rayTrace(
        RayTraceContext(
            vec3d,
            vec3d2,
            RayTraceContext.ShapeType.OUTLINE,
            RayTraceContext.FluidHandling.ANY,
            player
        )
    )
}

