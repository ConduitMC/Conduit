package systems.conduit.api;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.Scoreboard;

import systems.conduit.api.inventory.ChestContainer;

/**
 * Base Conduit interface for use with systems.conduit.core.mixins.
 * Implementation: {@link systems.conduit.core.mixins.api.PlayerMixin}
 *
 * @since API 0.1
 */
public interface Player extends LivingEntity {

    String getName();
    void closeOpenedContainer();
    void openContainer(ChestContainer container);

    Scoreboard getScoreboard();
    boolean isCreative();
    boolean isSpectator();
    boolean isSwimming();

    Iterable<ItemStack> getArmorSlots();
    Iterable<ItemStack> getHandSlots();

    boolean addItem(ItemStack item);
    void setItemSlot(EquipmentSlot slot, ItemStack item);

    boolean isHurt();
    boolean mayBuild();
    boolean canEat(boolean state);
    FoodData getFoodData();
    void causeFoodExhaustion(float exhaustion);

    int getXpNeededForNextLevel();
    void giveExperienceLevels(int levels);
    void giveExperiencePoints(int points);

    void killed(net.minecraft.world.entity.LivingEntity entity);
    void setRespawnPosition(BlockPos pos, boolean forced);
    boolean isRespawnForced();
    BlockPos getRespawnPosition();
    boolean isSleepingLongEnough();
    void stopSleeping();

    int getScore();
    void setScore(int score);
    void increaseScore(int score);
    int conduit_getFireImmuneTicks();

    void playSound(SoundEvent sound, float volume, float pitch);
    int getPortalWaitTime();
}
