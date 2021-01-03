package systems.conduit.main.api.mixins;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Material;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Base Conduit interface for use with mixins.
 * Implementation: {@link systems.conduit.main.mixins.entity.LivingEntityMixin}
 *
 * @since API 0.1
 */
public interface LivingEntity extends Entity {

    float getHealth();
    void setHealth(float health);

    void setMaxHealth(float health);
    float getMaxHealth();

    boolean isSleeping();

    void kill();
    boolean canBreatheUnderwater();
    boolean isBaby();
    float getScale();
    boolean rideableUnderWater();
    Optional<LivingEntity> conduit_getLastHurtByMob();
    int getLastHurtByMobTimestamp();
    void conduit_setLastHurtByMob(LivingEntity entity);
    void conduit_removeEffectParticles();
    boolean removeAllEffects();
    Collection<MobEffectInstance> getActiveEffects();
    Map<MobEffect, MobEffectInstance> getActiveEffectsMap();
    boolean hasEffect(MobEffect effect);
    boolean addEffect(MobEffectInstance effect);
    boolean removeEffect(MobEffect effect);
    boolean isInvertedHealAndHarm();
    void heal(float amount);
    Optional<LivingEntity> conduit_getKillCredit();
    int getArrowCount();
    void setArrowCount(int count);
    MobType getMobType();
    ItemStack getMainHandItem();
    ItemStack getOffhandItem();
    ItemStack getItemInHand(InteractionHand hand);
    void setItemInHand(InteractionHand hand, ItemStack item);
    boolean hasItemInSlot(EquipmentSlot slot);
    void setSprinting(boolean isSprinting);
    float getSpeed();
    void setSpeed(float speed);
    boolean isInWall();
    void eat(Level level, Material consumed);
    void onEffectAdded(MobEffectInstance effect);
    void onEffectRemoved(MobEffectInstance effect);
}
