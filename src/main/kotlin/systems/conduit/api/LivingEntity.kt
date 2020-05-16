package systems.conduit.api

import net.minecraft.world.InteractionHand
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.MobType
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.material.Material

/**
 * Base Conduit interface for use with mixins.
 * Implementation: [systems.conduit.core.mixins.api.LivingEntityMixin]
 *
 * @since API 0.1
 */
interface LivingEntity: Entity {
    fun getHealth(): Float
    fun setHealth(health: Float)

    fun setMaxHealth(health: Float)
    fun getMaxHealth(): Float

    fun isSleeping(): Boolean

    override fun kill()
    fun canBreatheUnderwater(): Boolean
    fun isBaby(): Boolean
    fun getScale(): Float
    fun rideableUnderWater(): Boolean
    fun conduit_getLastHurtByMob(): LivingEntity?
    fun getLastHurtByMobTimestamp(): Int
    fun conduit_setLastHurtByMob(entity: LivingEntity?)
    fun conduit_removeEffectParticles()
    fun removeAllEffects(): Boolean
    fun getActiveEffects(): Collection<MobEffectInstance?>?
    fun getActiveEffectsMap(): Map<MobEffect?, MobEffectInstance?>?
    fun hasEffect(effect: MobEffect?): Boolean
    fun addEffect(effect: MobEffectInstance?): Boolean
    fun removeEffect(effect: MobEffect?): Boolean
    fun isInvertedHealAndHarm(): Boolean
    fun heal(amount: Float)
    fun conduit_getKillCredit(): LivingEntity?
    fun getArrowCount(): Int
    fun setArrowCount(count: Int)
    fun getMobType(): MobType?
    fun getMainHandItem(): ItemStack?
    fun getOffhandItem(): ItemStack?
    fun getItemInHand(hand: InteractionHand?): ItemStack?
    fun setItemInHand(hand: InteractionHand?, item: ItemStack?)
    fun hasItemInSlot(slot: EquipmentSlot?): Boolean
    override fun setSprinting(isSprinting: Boolean)
    fun getSpeed(): Float
    fun setSpeed(speed: Float)
    override fun isInWall(): Boolean
    fun eat(level: Level?, consumed: Material?)
    fun onEffectAdded(effect: MobEffectInstance?)
    fun onEffectRemoved(effect: MobEffectInstance?)
}