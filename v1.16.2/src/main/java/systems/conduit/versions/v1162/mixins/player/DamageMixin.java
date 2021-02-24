package systems.conduit.versions.v1162.mixins.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
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
import systems.conduit.main.Conduit;
import systems.conduit.main.core.events.types.PlayerEvents;

/**
 * @author Innectic
 * @since 2/13/2021
 */

@Mixin(value = ServerPlayer.class, remap = false)
public abstract class DamageMixin extends Player {

    @Shadow
    public abstract boolean isInvulnerableTo(DamageSource damageSource);

    public DamageMixin(Level level, BlockPos pos, float yRot, GameProfile profile) {
        super(level, pos, yRot, profile);
    }

    @Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
    private void hurt(DamageSource source, float damage, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayer damaged = (ServerPlayer) (Object) this;

        if (source instanceof EntityDamageSource) {
            // Player was damaged by another entity. Lets see if we can narrow it down before calling it a generic damage event.
            EntityDamageSource entitySource = (EntityDamageSource) source;
            Entity entity = entitySource.getEntity();
            PlayerEvents.DamageMeta meta = new PlayerEvents.DamageMeta(entitySource.isThorns(), entitySource.isBypassArmor(),
                    entitySource.isBypassMagic(), entitySource.isBypassInvul(), entitySource.isCreativePlayer(), entitySource.isExplosion(),
                    entitySource.isFire(), entitySource.isMagic(), entitySource.isProjectile());

            if (entity instanceof AbstractArrow) {
                // Player was damaged by an arrow.
                AbstractArrow arrow = (AbstractArrow) entitySource.getEntity();
                if (arrow == null) return;

                PlayerEvents.PlayerDamageByArrowEvent event = new PlayerEvents.PlayerDamageByArrowEvent((systems.conduit.main.core.api.mixins.ServerPlayer) damaged, arrow.getOwner(), arrow, damage, meta);
                Conduit.getEventManager().dispatchEvent(event);
                if (event.isCanceled()) cir.cancel();
                return;
            } else if (entity instanceof ServerPlayer) {
                // Player was attached by another player
                ServerPlayer damager = (ServerPlayer) entitySource.getEntity();
                PlayerEvents.PlayerDamageByPlayerEvent event = new PlayerEvents.PlayerDamageByPlayerEvent((systems.conduit.main.core.api.mixins.ServerPlayer) damaged, (systems.conduit.main.core.api.mixins.ServerPlayer) damager, damage, meta);
                Conduit.getEventManager().dispatchEvent(event);
                if (event.isCanceled()) cir.cancel();
                return;
            }

            PlayerEvents.PlayerDamageByEntityEvent event = new PlayerEvents.PlayerDamageByEntityEvent((systems.conduit.main.core.api.mixins.ServerPlayer) damaged, entitySource.getEntity(), damage, meta);
            Conduit.getEventManager().dispatchEvent(event);
            if (event.isCanceled()) cir.cancel();
        }
    }
}
