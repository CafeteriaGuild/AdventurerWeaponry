package io.github.cafeteriaguild.advweaponry.mixin;

import io.github.cafeteriaguild.advweaponry.Weaponry;
import io.github.cafeteriaguild.advweaponry.items.modifiers.Modifier;
import io.github.cafeteriaguild.advweaponry.items.modifiers.Modifiers;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.struct.ModifyVariableInjectionInfo;

@Mixin(BowItem.class)
public abstract class MixinBowItem extends Item {
    public MixinBowItem(Settings settings) {
        super(settings);
    }

    @Shadow
    public abstract int getMaxUseTime(ItemStack stack);

    @Shadow
    public static float getPullProgress(int useTicks) {
        return 0;
    }

    @Redirect(method = "onStoppedUsing", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/item/BowItem;getPullProgress(I)F"
    ))
    public float proxyPullProgressSpeed(int useTicks, ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        float f = getPullProgress(useTicks);
        if (stack.getTag() != null && stack.getTag().contains("AWModifiers")) {
            ListTag listTag = stack.getTag().getList("AWModifiers", 10);
            for (Tag tag : listTag) {
                CompoundTag compoundTag = (CompoundTag) tag;
                Modifier modifier = Weaponry.INSTANCE.getModifierRegistry().get(new Identifier(compoundTag.getString("Id")));
                if (modifier == Modifiers.INSTANCE.getDrawTimeReducer()) {
                    int lvl = compoundTag.getInt("Lvl");
                    f += lvl / 10f;
                }
            }
        }
        return Math.min(f, 1.0f);
    }
}