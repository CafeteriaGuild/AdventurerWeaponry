package io.github.cafeteriaguild.advweaponry.blockentities

import io.github.cafeteriaguild.advweaponry.AWBlocks
import io.github.cafeteriaguild.advweaponry.blocks.logic.CraftingLogic
import io.github.cafeteriaguild.advweaponry.blocks.logic.PossibleRecipe
import io.github.cafeteriaguild.advweaponry.gui.widgets.AWSlot
import io.github.cafeteriaguild.advweaponry.inventory.AWInventory
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable
import net.minecraft.block.BlockState
import net.minecraft.block.InventoryProvider
import net.minecraft.block.entity.BlockEntity
import net.minecraft.inventory.SidedInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.util.Tickable
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldAccess

class AdvTableBlockEntity : BlockEntity(AWBlocks.advTableBlockEntity), BlockEntityClientSerializable, InventoryProvider, Tickable {
    // MATERIALS
    private val inventory = AWInventory(9) { _, _ -> true }
    // MODIFIERS
    val modifiersInv = AWInventory(8) { _, _ -> true }
    // INPUT/OUTPUT ITEM
    val ioInv = AWInventory(2) { slot, _ -> slot == 0 }
    val output = AWSlot(ioInv, 1, 7 * 18, (2.5 * 18).toInt())
    // POSSIBLE OUTPUTS
    val possibleOutputsInv = AWInventory(13) { _, _  -> true }
    var selectedSlot: Int = -1
    private var possibleRecipes: List<List<PossibleRecipe>> = listOf()
    private var currentPage = 0

    override fun tick() {
        if (world?.isClient == true) return
        val stacks = (0 until 9).map { slot -> inventory.getStack(slot) }
        possibleRecipes = CraftingLogic.recipesForItems(stacks)
        currentPage = 0
        if (possibleRecipes.isNotEmpty()) {
            val items = possibleRecipes[currentPage]
            (0 until 12).forEach { slot ->
                if (items.size > slot) {
                    val recipe = items[slot]
                    possibleOutputsInv.setStack(slot, ItemStack(recipe.recipe.output))
                } else possibleOutputsInv.setStack(slot, ItemStack.EMPTY)
            }
            ioInv.setStack(1, possibleOutputsInv.getStack(selectedSlot).copy())
            output.takeListener = {
                if (items.size > selectedSlot)
                    items[selectedSlot].remainingStacks.forEachIndexed { index, itemStack ->
                        inventory.setStack(index, itemStack)
                    }
            }
            markDirty()
            sync()
        } else {
            ioInv.setStack(1, ItemStack.EMPTY)
            possibleOutputsInv.clear()
        }
    }

    override fun getInventory(state: BlockState?, world: WorldAccess?, pos: BlockPos?): SidedInventory = inventory

    override fun fromTag(state: BlockState, tag: CompoundTag?) {
        val tagList = tag?.get("Inventory") as ListTag? ?: ListTag()
        tagList.indices.forEach { i ->
            val stackTag = tagList.getCompound(i)
            val slot = stackTag.getInt("Slot")
            getInventory(null, null, null).setStack(slot, ItemStack.fromTag(stackTag))
        }
        selectedSlot = tag?.getInt("SelectedSlot") ?: -1
        super.fromTag(state, tag)
    }

    override fun toTag(tag: CompoundTag): CompoundTag {
        val tagList = ListTag()
        val inventory = getInventory(null, null, null)
        for (i in 0 until inventory.size()) {
            val stackTag = CompoundTag()
            stackTag.putInt("Slot", i)
            tagList.add(inventory.getStack(i).toTag(stackTag))
        }
        tag.put("Inventory", tagList)
        tag.putInt("SelectedSlot", selectedSlot)
        return super.toTag(tag)
    }

    override fun toClientTag(tag: CompoundTag): CompoundTag {
        val tagList = ListTag()
        val inventory = getInventory(null, null, null)
        for (i in 0 until inventory.size()) {
            val stackTag = CompoundTag()
            stackTag.putInt("Slot", i)
            tagList.add(inventory.getStack(i).toTag(stackTag))
        }
        tag.put("Inventory", tagList)
        tag.putInt("SelectedSlot", selectedSlot)
        return tag
    }

    override fun fromClientTag(tag: CompoundTag) {
        val tagList = tag.get("Inventory") as ListTag? ?: ListTag()
        tagList.indices.forEach { i ->
            val stackTag = tagList.getCompound(i)
            val slot = stackTag.getInt("Slot")
            getInventory(null, null, null).setStack(slot, ItemStack.fromTag(stackTag))
        }
        selectedSlot = tag.getInt("SelectedSlot") ?: -1
    }
}