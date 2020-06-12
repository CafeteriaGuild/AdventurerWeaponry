package io.github.cafeteriaguild.advweaponry.mixin;

import io.github.cafeteriaguild.advweaponry.AWEntities;
import io.github.cafeteriaguild.advweaponry.entities.FreezingBombEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {
    @Inject(at = @At("TAIL"), method="onEntitySpawn")
    public void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo cbi) {
        ClientPlayNetworkHandler cpnh = (ClientPlayNetworkHandler) (Object) this;

        if (packet.getEntityTypeId() == AWEntities.INSTANCE.getFreezingBombEntityType()) {
            FreezingBombEntity e = new FreezingBombEntity(AWEntities.INSTANCE.getFreezingBombEntityType(), cpnh.getWorld());

            int i = packet.getId();
            e.setPos(packet.getX(), packet.getY(), packet.getZ());
            e.updateTrackedPosition(packet.getX(), packet.getY(), packet.getZ());
            e.pitch = (float)(packet.getPitch() * 360) / 256.0F;
            e.yaw = (float)(packet.getYaw() * 360) / 256.0F;
            e.setEntityId(i);
            e.setUuid(packet.getUuid());
            cpnh.getWorld().addEntity(i, e);
        }
    }
}
