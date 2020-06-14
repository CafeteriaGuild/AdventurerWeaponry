package io.github.cafeteriaguild.advweaponry.mixin;

import io.github.cottonmc.cotton.gui.SyncedGuiDescription;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SyncedGuiDescription.class)
public interface AccessorSyncedGuiDescription {
    @Invoker
    boolean callInsertItem(ItemStack toInsert, Inventory inventory, boolean walkBackwards, PlayerEntity player);
}
