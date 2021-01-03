package systems.conduit.main.mixins.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.mixins.Player;
import systems.conduit.main.api.mixins.ServerPlayer;
import systems.conduit.main.api.mixins.player.Abilities;
import systems.conduit.main.core.events.types.PlayerEvents;

@Mixin(value = net.minecraft.world.entity.player.Player.class, remap = false)
public abstract class PlayerMixin implements Player {

    @Accessor public abstract AbstractContainerMenu getContainerMenu();
    @Shadow @Final public InventoryMenu inventoryMenu;
    @Shadow public abstract Inventory getInventory();
    @Shadow public abstract Iterable<ItemStack> getArmorSlots();
    @Shadow public abstract Iterable<ItemStack> getHandSlots();
    @Shadow public abstract boolean addItem(ItemStack item);
    @Shadow public abstract void setItemSlot(EquipmentSlot slot, ItemStack item);

    @Shadow public abstract GameProfile getGameProfile();
    @Shadow protected abstract void closeContainer();
    @Shadow public abstract Scoreboard getScoreboard();

    @Shadow public abstract boolean isCreative();
    @Shadow public abstract boolean isSpectator();
    @Shadow public abstract boolean isSwimming();

    @Shadow public abstract boolean isHurt();
    @Shadow public abstract boolean mayBuild();
    @Shadow public abstract boolean canEat(boolean state);
    @Shadow public abstract FoodData getFoodData();
    @Shadow public abstract void causeFoodExhaustion(float exhaustion);

    @Shadow public abstract int getXpNeededForNextLevel();
    @Shadow public abstract void giveExperienceLevels(int levels);
    @Shadow public abstract void giveExperiencePoints(int points);

    @Shadow public abstract boolean isSleepingLongEnough();
    @Shadow public abstract void stopSleeping();

    @Shadow public abstract int getScore();
    @Shadow public abstract void setScore(int score);
    @Shadow public abstract void increaseScore(int score);
    @Shadow public abstract void die(DamageSource source);
    @Shadow public abstract float getDestroySpeed(BlockState state);
    @Shadow public abstract boolean hasCorrectToolForDrops(BlockState state);
    @Shadow public abstract void addAdditionalSaveData(CompoundTag tag);
    @Shadow public abstract boolean isInvulnerableTo(DamageSource source);
    @Shadow public abstract InteractionResult interactOn(Entity entity, InteractionHand hand);
    @Shadow public abstract boolean mayUseItemAt(BlockPos pos, Direction direction, ItemStack with);
    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot slot);

    @Shadow public abstract double getMyRidingOffset();
    @Shadow public abstract void removeVehicle();
    @Shadow public abstract boolean isAffectedByFluids();
    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);
    @Shadow public abstract int getPortalWaitTime();
    @Shadow @Final private net.minecraft.world.entity.player.Abilities abilities;
    @Shadow public abstract void onUpdateAbilities();
    @Shadow protected abstract boolean isAboveGround();
    @Shadow public abstract void awardStat(ResourceLocation location);
    @Shadow public abstract void awardStat(ResourceLocation location, int amount);
    @Shadow public abstract void updateSwimming();
    @Shadow public abstract float getSpeed();
    @Shadow public abstract int getEnchantmentSeed();

    @Shadow public abstract boolean setEntityOnShoulder(CompoundTag tag);
    @Shadow protected abstract void removeEntitiesOnShoulder();
    @Shadow protected abstract void respawnEntityOnShoulder(CompoundTag tag);

    @Shadow public abstract float getStandingEyeHeight(Pose pose, EntityDimensions dimensions);
    @Shadow public abstract float getAbsorptionAmount();
    @Shadow public abstract void setAbsorptionAmount(float amount);
    @Shadow public abstract void setRemainingFireTicks(int ticks);

    @Shadow public abstract HumanoidArm getMainArm();
    @Shadow public abstract CompoundTag getShoulderEntityLeft();
    @Shadow public abstract CompoundTag getShoulderEntityRight();
    @Shadow protected abstract void setShoulderEntityLeft(CompoundTag tag);
    @Shadow protected abstract void setShoulderEntityRight(CompoundTag tag);

    @Shadow public abstract ItemCooldowns getCooldowns();
    @Shadow public abstract float getLuck();
    @Shadow public abstract boolean canUseGameMasterBlocks();

    @Override
    public void conduit_setShoulderEntityLeft(CompoundTag tag) {
        setShoulderEntityLeft(tag);
    }

    @Override
    public void conduit_setShoulderEntityRight(CompoundTag tag) {
        setShoulderEntityRight(tag);
    }

    @Override
    public void conduit_removeEntitiesOnShoulder() {
        removeEntitiesOnShoulder();
    }

    @Override
    public void conduit_respawnEntityOnShoulder(CompoundTag tag) {
        respawnEntityOnShoulder(tag);
    }

    @Override
    public boolean conduit_isAboveGround() {
        return isAboveGround();
    }

    public void closeOpenedContainer() {
        if (this.getContainerMenu() != this.inventoryMenu) {
            this.closeContainer();
        }
    }

    @Override
    public String getName() {
        return getGameProfile().getName();
    }

    @Override
    public Abilities getAbilities() {
        return (Abilities) this.abilities;
    }

    @Inject(method = "interactOn", at = @At("HEAD"))
    public void interactOn(Entity entity, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        PlayerEvents.EntityClickEvent event = new PlayerEvents.EntityClickEvent((ServerPlayer) ((Object) this), this.getItemInHand(interactionHand), interactionHand, (systems.conduit.main.api.mixins.Entity) entity);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            cir.setReturnValue(InteractionResult.PASS);
            cir.cancel();
        }
    }

    @Inject(method = "setMainArm", at = @At("HEAD"))
    public void setMainArm(HumanoidArm humanoidArm, CallbackInfo ci) {
        PlayerEvents.MainHandChangeEvent event = new PlayerEvents.MainHandChangeEvent((ServerPlayer) (Object) this, humanoidArm);
        Conduit.getEventManager().dispatchEvent(event);
    }

    @Inject(method = "setEntityOnShoulder", at = @At(value = "HEAD", target = "Lnet/minecraft/world/entity/player/Player;setShoulderEntityLeft(Lnet/minecraft/nbt/CompoundTag;)V"))
    public void setEntityOnLeftShoulder(CompoundTag compoundTag, CallbackInfoReturnable<Boolean> cir) {
        PlayerEvents.EntityRideShoulderEvent event = new PlayerEvents.EntityRideShoulderEvent((Player) (Object) this, compoundTag, PlayerEvents.EntityRideShoulderEvent.Shoulder.LEFT);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "setEntityOnShoulder", at = @At(value = "HEAD", target = "Lnet/minecraft/world/entity/player/Player;setShoulderEntityRight(Lnet/minecraft/nbt/CompoundTag;)V"))
    public void setEntityOnRightShoulder(CompoundTag compoundTag, CallbackInfoReturnable<Boolean> cir) {
        PlayerEvents.EntityRideShoulderEvent event = new PlayerEvents.EntityRideShoulderEvent((Player) (Object) this, compoundTag, PlayerEvents.EntityRideShoulderEvent.Shoulder.RIGHT);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
