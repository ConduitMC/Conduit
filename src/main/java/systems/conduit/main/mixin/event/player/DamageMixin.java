package systems.conduit.main.mixin.event.player;

import com.mojang.authlib.GameProfile;

import systems.conduit.main.Conduit;
import systems.conduit.main.events.EventType;

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

@Mixin(value = ServerPlayer.class, remap = false)
public abstract class DamageMixin extends Player {

    @Shadow public abstract boolean isInvulnerableTo(DamageSource damageSource);

    public DamageMixin(Level level, GameProfile gameProfile) {
        super(level, gameProfile);
    }

    @Inject(at = @At("HEAD"), method = "hurt")
    private void hurt(DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayer damaged = (ServerPlayer) (Object) this;

        systems.conduit.main.api.Player p =(systems.conduit.main.api.Player) damaged;
        p.sendMessage(String.valueOf(p.getMaxHealth()));

        if (source instanceof EntityDamageSource) {
            // Player was damaged by another entity. Lets see if we can narrow it down before calling it a generic damage event.
            EntityDamageSource entitySource = (EntityDamageSource) source;
            Entity entity = entitySource.getEntity();
            EventType.DamageMeta meta = new EventType.DamageMeta(entitySource.isThorns(), entitySource.isBypassArmor(),
                    entitySource.isBypassMagic(), entitySource.isBypassInvul(), entitySource.isCreativePlayer(), entitySource.isExplosion(),
                    entitySource.isFire(), entitySource.isMagic(), entitySource.isProjectile());

            if (entity instanceof AbstractArrow) {
                // Player was damaged by an arrow.
                AbstractArrow arrow = (AbstractArrow) entitySource.getEntity();
                if (arrow == null) return;

                EventType.PlayerDamageByArrowEvent event = new EventType.PlayerDamageByArrowEvent((systems.conduit.main.api.Player) damaged, arrow.getOwner(), arrow, damage, meta);
                Conduit.eventManager.dispatchEvent(event);
                return;
            } else if (entity instanceof ServerPlayer) {
                // Player was attached by another player
                ServerPlayer damager = (ServerPlayer) entitySource.getEntity();
                EventType.PlayerDamageByPlayerEvent event = new EventType.PlayerDamageByPlayerEvent((systems.conduit.main.api.Player) damaged, (systems.conduit.main.api.Player) damager, damage, meta);
                Conduit.eventManager.dispatchEvent(event);
                return;
            }

            EventType.PlayerDamageByEntityEvent event = new EventType.PlayerDamageByEntityEvent((systems.conduit.main.api.Player) damaged, entitySource.getEntity(), damage, meta);
            Conduit.eventManager.dispatchEvent(event);
        }
    }
}
