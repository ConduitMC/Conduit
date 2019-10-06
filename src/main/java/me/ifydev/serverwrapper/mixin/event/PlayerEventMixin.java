package me.ifydev.serverwrapper.mixin.event;

import com.mojang.authlib.GameProfile;
import me.ifydev.serverwrapper.ServerWrapper;
import me.ifydev.serverwrapper.events.EventType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Innectic
 * @since 10/5/2019
 */
@Mixin(value = ServerPlayer.class, remap = false)
public abstract class PlayerEventMixin extends Player {

    @Shadow public abstract boolean isInvulnerableTo(DamageSource damageSource);

    public PlayerEventMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(at = @At("HEAD"), method = "hurt")
    public void hurt(DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayer damaged = (ServerPlayer) (Object) this;
        ServerWrapper.LOGGER.info("A");

        if (source instanceof EntityDamageSource) {
            // Player was damaged by another entity. Lets see if we can narrow it down before calling it a generic damage event.
            EntityDamageSource entitySource = (EntityDamageSource) source;
            Entity entity = entitySource.getEntity();
            ServerWrapper.LOGGER.info("B");

            if (entity instanceof AbstractArrow) {
                // Player was damaged by an arrow.
                ServerWrapper.LOGGER.info("C");
                AbstractArrow arrow = (AbstractArrow) entitySource.getEntity();
                EventType.PlayerDamageByArrowEvent event = new EventType.PlayerDamageByArrowEvent(damaged, arrow.getOwner(), arrow, damage);
                ServerWrapper.eventManager.dispatchEvent(event);
                return;
            } else if (entity instanceof ServerPlayer) {
                // Player was attached by another player
                ServerWrapper.LOGGER.info("C");
                ServerPlayer damager = (ServerPlayer) entitySource.getEntity();
                EventType.PlayerDamageByPlayerEvent event = new EventType.PlayerDamageByPlayerEvent(damaged, damager, damage);
                ServerWrapper.eventManager.dispatchEvent(event);
                return;
            }

            ServerWrapper.LOGGER.info("D");
            EventType.PlayerDamageByEntityEvent event = new EventType.PlayerDamageByEntityEvent(damaged, entitySource.getEntity(), damage);
            ServerWrapper.eventManager.dispatchEvent(event);
        }
    }
}
