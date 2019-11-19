package systems.conduit.main.api;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.scores.Scoreboard;

/**
 * Base Conduit interface for use with mixins.
 *
 * @since API 0.1
 *
 * Implementation: {@link systems.conduit.main.mixins.api.PlayerMixin}
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
    int getImmuneFireTicks();

    void playSound(SoundEvent sound, float volume, float pitch);
    int getPortalWaitTime();
}
