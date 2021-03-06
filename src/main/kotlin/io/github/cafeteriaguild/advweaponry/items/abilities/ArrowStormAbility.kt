package io.github.cafeteriaguild.advweaponry.items.abilities

import io.github.cafeteriaguild.advweaponry.rayTrace
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.projectile.PersistentProjectileEntity
import net.minecraft.item.ArrowItem
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Box
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class ArrowStormAbility : Ability() {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val stack = user.getStackInHand(hand)
        val hitResult = rayTrace(world, user, 20.0) ?: return TypedActionResult.pass(stack)
        if (hitResult.type == HitResult.Type.MISS) return TypedActionResult.pass(stack)
        val hitPos = hitResult.blockPos
        val entities = world.getEntities(null, Box(hitPos.add(2, 2, 2), hitPos.add(-2, -2, -2)))

        val target = if (entities.isNotEmpty()) {
            var x = 0.0
            var y = 0.0
            var z = 0.0
            for (e in entities) {
                x += e.x
                y += e.y
                z += e.z
            }
            val size = entities.size
            Vec3d(x / size, y / size, z / size)
        } else Vec3d.of(hitPos)

        if (hitPos.isWithinDistance(user.pos, 30.0) && world.isSkyVisible(hitPos.up()) && stack.cooldown <= 0) {
            for (y in 0..5 + user.random.nextInt(8)) {
                val arrowItem = Items.ARROW as ArrowItem
                val arrowStack = ItemStack(arrowItem)
                val arrowEntity = arrowItem.createArrow(world, arrowStack, user)
                arrowEntity.setPos(
                    target.x - 1.5 + user.random.nextDouble() * 2,
                    target.y + 80 + y.toDouble() * 2 + user.random.nextDouble() - 0.5,
                    target.z - 1.5 + user.random.nextDouble() * 2
                )
                arrowEntity.setProperties(user, 90f, 0f, 0f, 3.0f, 1.0f)
                arrowEntity.isCritical = true
                arrowEntity.pickupType = PersistentProjectileEntity.PickupPermission.DISALLOWED
                world.spawnEntity(arrowEntity)
            }
            stack.cooldown += 30
            return TypedActionResult.success(stack)
        }

        return super.use(world, user, hand)
    }
}