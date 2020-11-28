package systems.conduit.main.mixins.api;

import com.mojang.authlib.GameProfile;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.Scoreboard;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import systems.conduit.main.api.Player;

@Mixin(value = net.minecraft.world.entity.player.Player.class, remap = false)
public abstract class PlayerMixin implements Player {

    @Shadow public AbstractContainerMenu containerMenu;
    @Shadow @Final public InventoryMenu inventoryMenu;
    //@Shadow private BlockPos respawnPosition;
    //@Shadow private boolean respawnForced;

    @Shadow public abstract GameProfile getGameProfile();
    @Shadow protected abstract void closeContainer();
    @Shadow public abstract Scoreboard getScoreboard();

    @Shadow public abstract boolean isCreative();
    @Shadow public abstract boolean isSpectator();
    @Shadow public abstract boolean isSwimming();

    @Shadow public abstract Iterable<ItemStack> getArmorSlots();
    @Shadow public abstract Iterable<ItemStack> getHandSlots();

    @Shadow public abstract boolean addItem(ItemStack item);
    @Shadow public abstract void setItemSlot(EquipmentSlot slot, ItemStack item);

    @Shadow public abstract boolean isHurt();
    @Shadow public abstract boolean mayBuild();
    @Shadow public abstract boolean canEat(boolean state);
    @Shadow public abstract FoodData getFoodData();
    @Shadow public abstract void causeFoodExhaustion(float exhaustion);

    @Shadow public abstract int getXpNeededForNextLevel();
    @Shadow public abstract void giveExperienceLevels(int levels);
    @Shadow public abstract void giveExperiencePoints(int points);

    //@Shadow public abstract boolean isRespawnForced();
    //@Shadow public abstract BlockPos getRespawnPosition();
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

    /**
    @Override
    public void setRespawnPosition(BlockPos pos, boolean forced) {
        if (pos != null) {
            if (!pos.equals(this.respawnPosition)) {
                this.sendMessage(new TranslatableComponent("block.minecraft.bed.set_spawn"));
            }
            this.respawnPosition = pos;
            this.respawnForced = forced;
        } else {
            this.respawnPosition = null;
            this.respawnForced = false;
        }
    }
    */

    public void closeOpenedContainer() {
        if (this.containerMenu != this.inventoryMenu) {
            this.closeContainer();
        }
    }

    @Override
    public String getName() {
        return getGameProfile().getName();
    }

    @Override
    public AbstractContainerMenu getContainerMenu() {
        return containerMenu;
    }
}
