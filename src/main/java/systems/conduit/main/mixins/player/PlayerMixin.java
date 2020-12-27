package systems.conduit.main.mixins.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
    @Shadow protected abstract int shadow$getFireImmuneTicks();

    public int conduit_getFireImmuneTicks() {
        return this.shadow$getFireImmuneTicks();
    }

    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);
    @Shadow public abstract int getPortalWaitTime();

    @Shadow @Final private net.minecraft.world.entity.player.Abilities abilities;

    @Shadow public abstract void onUpdateAbilities();

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
        PlayerEvents.InteractAtEntityEvent event = new PlayerEvents.InteractAtEntityEvent((ServerPlayer) ((Object) this), this.getItemInHand(interactionHand), interactionHand, (systems.conduit.main.api.mixins.Entity) entity);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            cir.setReturnValue(InteractionResult.PASS);
            cir.cancel();
        }
    }
}
