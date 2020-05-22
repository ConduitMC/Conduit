package systems.conduit.core.mixins.api

import net.minecraft.world.InteractionHand
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.MobType
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.attributes.AttributeInstance
import net.minecraft.world.entity.monster.SharedMonsterAttributes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import systems.conduit.api.LivingEntity
import systems.conduit.api.Player
import systems.conduit.core.Conduit.eventManager
import systems.conduit.core.events.types.EntityEvents.EffectAddedToEntityEvent
import systems.conduit.core.events.types.EntityEvents.EffectRemovedFromEntityEvent
import systems.conduit.core.events.types.PlayerEvents.ConsumeEvent
import java.util.*


@Mixin(value = [net.minecraft.world.entity.LivingEntity::class])
abstract class LivingEntityMixin: LivingEntity {
    @Shadow
    abstract override fun getHealth(): Float

    @Shadow
    abstract override fun setHealth(health: Float)

    @Shadow
    @Final
    abstract override fun getMaxHealth(): Float

    @Shadow
    abstract fun getAttribute(attribute: Attribute?): AttributeInstance

    @Shadow
    abstract override fun isSleeping(): Boolean

    @Shadow
    abstract override fun kill()

    @Shadow
    abstract override fun canBreatheUnderwater(): Boolean

    @Shadow
    abstract override fun isBaby(): Boolean

    @Shadow
    abstract override fun getScale(): Float

    @Shadow
    abstract override fun rideableUnderWater(): Boolean

    @Shadow
    abstract fun `shadow$getLastHurtByMob`(): net.minecraft.world.entity.LivingEntity?

    @Shadow
    abstract override fun getLastHurtByMobTimestamp(): Int

    @Shadow
    abstract fun `shadow$setLastHurtByMob`(entity: net.minecraft.world.entity.LivingEntity?)

    @Shadow
    protected abstract fun `shadow$removeEffectParticles`()

    @Shadow
    abstract override fun removeAllEffects(): Boolean

    @Shadow
    abstract override fun getActiveEffects(): Collection<MobEffectInstance?>?

    @Shadow
    abstract override fun getActiveEffectsMap(): Map<MobEffect?, MobEffectInstance?>?

    @Shadow
    abstract override fun hasEffect(effect: MobEffect?): Boolean

    @Shadow
    abstract override fun addEffect(effect: MobEffectInstance?): Boolean

    @Shadow
    abstract override fun removeEffect(effect: MobEffect?): Boolean

    @Shadow
    abstract override fun isInvertedHealAndHarm(): Boolean

    @Shadow
    abstract override fun heal(amount: Float)

    @Shadow
    abstract fun `shadow$getKillCredit`(): net.minecraft.world.entity.LivingEntity?

    @Shadow
    abstract override fun getArrowCount(): Int

    @Shadow
    abstract override fun setArrowCount(count: Int)

    @Shadow
    abstract override fun getMobType(): MobType?

    @Shadow
    abstract override fun getMainHandItem(): ItemStack?

    @Shadow
    abstract override fun getOffhandItem(): ItemStack?

    @Shadow
    abstract override fun getItemInHand(hand: InteractionHand?): ItemStack?

    @Shadow
    abstract override fun setItemInHand(hand: InteractionHand?, item: ItemStack?)

    @Shadow
    abstract override fun hasItemInSlot(slot: EquipmentSlot?): Boolean

    @Shadow
    abstract override fun setSprinting(state: Boolean)

    @Shadow
    abstract override fun getSpeed(): Float

    @Shadow
    abstract override fun setSpeed(speed: Float)

    @Shadow
    abstract override fun isInWall(): Boolean

    override fun conduit_getKillCredit(): LivingEntity? {
        val entity = `shadow$getKillCredit`() ?: return null
        return entity as LivingEntity
    }

    override fun conduit_removeEffectParticles() {
        `shadow$removeEffectParticles`()
    }

    override fun conduit_getLastHurtByMob(): LivingEntity? {
        val entity = `shadow$getLastHurtByMob`() ?: return null
        return entity as LivingEntity
    }

    override fun setMaxHealth(health: Float) {
        getAttribute(SharedMonsterAttributes.MAX_HEALTH).baseValue = health.toDouble()
    }

    override fun conduit_setLastHurtByMob(entity: LivingEntity?) {
        if (entity == null) return
        `shadow$setLastHurtByMob`(entity as net.minecraft.world.entity.LivingEntity?)
    }

    @Inject(method = ["eat"], at = [At("HEAD")])
    fun eat(level: Level?, itemStack: ItemStack?, cir: CallbackInfoReturnable<ItemStack?>?) {
        if (this is Player) {
            val event = ConsumeEvent((this as Player), itemStack!!)
            eventManager.dispatchEvent(event)
        }
    }

    @Inject(method = ["onEffectAdded"], at = [At("HEAD")])
    fun onEffectAdded(effect: MobEffectInstance?, ci: CallbackInfo?) {
        val event = EffectAddedToEntityEvent(this, effect!!)
        eventManager.dispatchEvent(event)
    }

    @Inject(method = ["onEffectRemoved"], at = [At("HEAD")])
    fun onEffectRemoved(effect: MobEffectInstance?, ci: CallbackInfo?) {
        val event = EffectRemovedFromEntityEvent(this, effect!!)
        eventManager.dispatchEvent(event)
    }
}
