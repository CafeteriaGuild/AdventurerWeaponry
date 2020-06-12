package io.github.cafeteriaguild.advweaponry.items.abilities

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

open class Ability {
    open fun useOnBlock(context: ItemUsageContext): ActionResult {
        return ActionResult.PASS
    }

    open fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        return TypedActionResult.pass(user.getStackInHand(hand))
    }
}