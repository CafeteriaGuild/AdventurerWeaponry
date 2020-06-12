package io.github.cafeteriaguild.advweaponry.items

import io.github.cafeteriaguild.advweaponry.entities.FreezingBombEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.stat.Stats
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class AWFreezingBombItem(settings: Settings) : Item(settings) {
    override fun use(world: World?, user: PlayerEntity?, hand: Hand?): TypedActionResult<ItemStack> {
        val itemStack = user!!.getStackInHand(hand)
        world!!.playSound(null as PlayerEntity?, user.x, user.y, user.z, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (RANDOM.nextFloat() * 0.4f + 0.8f))
        if (!world.isClient) {
            val freezingBomb = FreezingBombEntity(world, user)
            freezingBomb.setItem(itemStack)
            freezingBomb.setProperties(user, user.pitch, user.yaw, 0.0f, 1.5f, 1.0f)
            world.spawnEntity(freezingBomb)
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this))
        if (!user.abilities.creativeMode) {
            itemStack.decrement(1)
        }

        return TypedActionResult.method_29237(itemStack, world!!.isClient())
    }
}