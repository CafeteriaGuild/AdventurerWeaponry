package io.github.cafeteriaguild.advweaponry.mixin;

import io.github.cottonmc.cotton.gui.ValidatedSlot;
import io.github.cottonmc.cotton.gui.widget.WItemSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(WItemSlot.class)
public interface AccessorWItemSlot {
    @Accessor
    int getStartIndex();
    @Accessor
    List<ValidatedSlot> getPeers();
}
