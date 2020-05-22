package systems.conduit.core.mixins.event.player

import com.mojang.authlib.GameProfile
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.EntityDamageSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.level.Level
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import systems.conduit.core.Conduit
import systems.conduit.core.events.types.PlayerEvents

@Mixin(value = [ServerPlayer::class], remap = false)
abstract class DamageMixin(level: Level?, gameProfile: GameProfile?): Player(level, gameProfile) {

    @Shadow abstract override fun isInvulnerableTo(damageSource: DamageSource): Boolean

    @Inject(at = [At("HEAD")], method = ["hurt"])
    private fun hurt(source: DamageSource, damage: Float, cir: CallbackInfoReturnable<Boolean>) {
        if (source is EntityDamageSource) {
            // Player was damaged by another entity. Lets see if we can narrow it down before calling it a generic damage event.
            val entity: Entity? = source.entity
            val meta = PlayerEvents.DamageMeta(source.isThorns, source.isBypassArmor(),
                    source.isBypassMagic(), source.isBypassInvul(), source.isCreativePlayer(), source.isExplosion(),
                    source.isFire(), source.isMagic(), source.isProjectile())
            if (entity is AbstractArrow) {
                // Player was damaged by an arrow.
                val event = PlayerEvents.PlayerDamageByArrowEvent(this as systems.conduit.api.ServerPlayer, entity.owner, entity, damage, meta)
                Conduit.eventManager.dispatchEvent(event)
                if (event.cancelled) cir.cancel()
                return
            } else if (entity is ServerPlayer) {
                // Player was attached by another player
                val event = PlayerEvents.PlayerDamageByPlayerEvent(this as systems.conduit.api.ServerPlayer, entity as systems.conduit.api.ServerPlayer, damage, meta)
                Conduit.eventManager.dispatchEvent(event)
                if (event.cancelled) cir.cancel()
                return
            }
            val event = PlayerEvents.PlayerDamageByEntityEvent(this as systems.conduit.api.ServerPlayer, source.getEntity(), damage, meta)
            Conduit.eventManager.dispatchEvent(event)
            if (event.cancelled) cir.cancel()
        }
    }
}