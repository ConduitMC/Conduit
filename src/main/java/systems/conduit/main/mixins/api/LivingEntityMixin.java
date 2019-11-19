package systems.conduit.main.mixins.api;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.SharedMonsterAttributes;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import systems.conduit.main.api.LivingEntity;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Mixin(value = net.minecraft.world.entity.LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntity {

    @Shadow public abstract float getHealth();
    @Shadow public abstract void setHealth(float health);

    @Shadow @Final public abstract float getMaxHealth();

    @Shadow public abstract AttributeInstance getAttribute(Attribute attribute);
    @Shadow public abstract boolean isSleeping();

    @Shadow public abstract void kill();
    @Shadow public abstract boolean canBreatheUnderwater();
    @Shadow public abstract boolean isBaby();
    @Shadow public abstract float getScale();
    @Shadow public abstract boolean rideableUnderWater();
    @Shadow public abstract net.minecraft.world.entity.LivingEntity shadow$getLastHurtByMob();
    @Shadow public abstract int getLastHurtByMobTimestamp();
    @Shadow public abstract void shadow$setLastHurtByMob(net.minecraft.world.entity.LivingEntity entity);
    @Shadow protected abstract void shadow$removeEffectParticles();
    @Shadow public abstract boolean removeAllEffects();
    @Shadow public abstract Collection<MobEffectInstance> getActiveEffects();
    @Shadow public abstract Map<MobEffect, MobEffectInstance> getActiveEffectsMap();
    @Shadow public abstract boolean hasEffect(MobEffect effect);
    @Shadow public abstract boolean addEffect(MobEffectInstance effect);
    @Shadow public abstract boolean removeEffect(MobEffect effect);
    @Shadow public abstract boolean isInvertedHealAndHarm();
    @Shadow public abstract void heal(float amount);
    @Shadow public abstract net.minecraft.world.entity.LivingEntity shadow$getKillCredit();
    @Shadow public abstract int getArrowCount();
    @Shadow public abstract void setArrowCount(int count);
    @Shadow public abstract MobType getMobType();
    @Shadow public abstract ItemStack getMainHandItem();
    @Shadow public abstract ItemStack getOffhandItem();
    @Shadow public abstract ItemStack getItemInHand(InteractionHand hand);
    @Shadow public abstract void setItemInHand(InteractionHand hand, ItemStack item);
    @Shadow public abstract boolean hasItemInSlot(EquipmentSlot slot);
    @Shadow public abstract void setSprinting(boolean isSprinting);
    @Shadow public abstract float getSpeed();
    @Shadow public abstract void setSpeed(float speed);
    @Shadow public abstract boolean isInWall();

    public Optional<LivingEntity> getKillCredit() {
        net.minecraft.world.entity.LivingEntity entity = shadow$getKillCredit();
        if (entity == null) return Optional.empty();
        return Optional.of((LivingEntity) entity);
    }

    public void removeEffectParticles() {
        this.shadow$removeEffectParticles();
    }

    public Optional<LivingEntity> getLastHurtByMob() {
        net.minecraft.world.entity.LivingEntity entity = this.shadow$getLastHurtByMob();
        if (entity == null) return Optional.empty();

        return Optional.of((LivingEntity) entity);
    }

    public void setMaxHealth(float health) {
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
    }

    public void setLastHurtByMob(LivingEntity entity) {
        if (entity == null) return;
        this.shadow$setLastHurtByMob((net.minecraft.world.entity.LivingEntity) entity);
    }
}
