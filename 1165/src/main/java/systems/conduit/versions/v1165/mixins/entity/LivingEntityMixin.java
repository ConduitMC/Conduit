package systems.conduit.versions.v1165.mixins.entity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.api.mixins.LivingEntity;
import systems.conduit.main.core.api.mixins.Player;
import systems.conduit.main.core.events.types.EntityEvents;
import systems.conduit.main.core.events.types.PlayerEvents;

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

    public Optional<LivingEntity> conduit_getKillCredit() {
        net.minecraft.world.entity.LivingEntity entity = shadow$getKillCredit();
        if (entity == null) return Optional.empty();
        return Optional.of((LivingEntity) entity);
    }

    public void conduit_removeEffectParticles() {
        this.shadow$removeEffectParticles();
    }

    public Optional<LivingEntity> conduit_getLastHurtByMob() {
        net.minecraft.world.entity.LivingEntity entity = this.shadow$getLastHurtByMob();
        if (entity == null) return Optional.empty();

        return Optional.of((LivingEntity) entity);
    }

    public void setMaxHealth(float health) {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(health);
    }

    public void conduit_setLastHurtByMob(LivingEntity entity) {
        if (entity == null) return;
        this.shadow$setLastHurtByMob((net.minecraft.world.entity.LivingEntity) entity);
    }

    @Inject(method = "eat", at = @At("HEAD"))
    public void eat(Level level, ItemStack itemStack, CallbackInfoReturnable<ItemStack> cir) {
        if (this instanceof Player) {
            PlayerEvents.ConsumeEvent event = new PlayerEvents.ConsumeEvent((Player) this, itemStack);
            Conduit.getEventManager().dispatchEvent(event);
        }
    }

    @Inject(method = "onEffectAdded", at = @At("HEAD"))
    public void onEffectAdded(MobEffectInstance effect, CallbackInfo ci) {
        EntityEvents.EffectAddedToEntityEvent event = new EntityEvents.EffectAddedToEntityEvent(this, effect);
        Conduit.getEventManager().dispatchEvent(event);
    }

    @Inject(method = "onEffectRemoved", at = @At("HEAD"))
    public void onEffectRemoved(MobEffectInstance effect, CallbackInfo ci) {
        EntityEvents.EffectRemovedFromEntityEvent event = new EntityEvents.EffectRemovedFromEntityEvent(this, effect);
        Conduit.getEventManager().dispatchEvent(event);
    }
}
