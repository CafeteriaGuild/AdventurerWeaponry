package io.github.cafeteriaguild.advweaponry.items

import io.github.cafeteriaguild.advweaponry.items.abilities.Ability
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.ItemUsageContext
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

open class AWStaffItem(private val ability: Ability, settings: Item.Settings) : Item(settings) {
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        return ability.useOnBlock(context)
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        return ability.use(world, user, hand)
    }
}