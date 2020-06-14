package io.github.cafeteriaguild.advweaponry.items

import io.github.cafeteriaguild.advweaponry.items.abilities.Ability
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.*
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class AWSwordItem(
    toolMaterial: ToolMaterial,
    attackDamage: Int,
    attackSpeed: Float,
    private val ability: Ability,
    settings: Item.Settings
) : SwordItem(toolMaterial, attackDamage, attackSpeed, settings) {
    override fun useOnBlock(context: ItemUsageContext): ActionResult {
        return ability.useOnBlock(context)
    }

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        return ability.use(world, user, hand)
    }
}