package systems.conduit.main.core.api.mixins;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.scores.Scoreboard;
import systems.conduit.main.core.api.mixins.player.Abilities;

/**
 * Base Conduit interface for use with mixins.
 *
 * @since API 0.1
 */
public interface Player extends LivingEntity {

    String getName();
    void closeOpenedContainer();
    AbstractContainerMenu getContainerMenu();

    Scoreboard getScoreboard();
    boolean isCreative();
    boolean isSpectator();
    boolean isSwimming();

    Iterable<ItemStack> getArmorSlots();
    Iterable<ItemStack> getHandSlots();

    boolean addItem(ItemStack item);
    void setItemSlot(EquipmentSlot slot, ItemStack item);
    ItemStack getItemBySlot(EquipmentSlot slot);

    boolean isHurt();
    boolean mayBuild();
    boolean canEat(boolean state);
    FoodData getFoodData();
    void causeFoodExhaustion(float exhaustion);

    int getXpNeededForNextLevel();
    void giveExperienceLevels(int levels);
    void giveExperiencePoints(int points);

    void setRespawnPosition(BlockPos pos, boolean forced);
    boolean isRespawnForced();
    BlockPos getRespawnPosition();
    boolean isSleepingLongEnough();
    void stopSleeping();
    void die();

    int getScore();
    void setScore(int score);
    void increaseScore(int score);
    int getFireImmuneTicks();

    void playSound(SoundEvent sound, float volume, float pitch);
    int getPortalWaitTime();

    Inventory getInventory();
    Abilities getAbilities();

    float getDestroySpeed(BlockState state);
    boolean hasCorrectToolForDrops(BlockState state);

    void addAdditionalSaveData(CompoundTag tag);
    boolean isInvulnerableTo(DamageSource source);

    InteractionResult interactOn(Entity entity, InteractionHand hand);

    double getMyRidingOffset();
    void removeVehicle();
    boolean isImmobile();
    boolean isAffectedByFluids();
    boolean conduit_isAboveGround();

    GameProfile getGameProfile();

    void awardStat(ResourceLocation location);
    void awardStat(ResourceLocation location, int amount);

    void updateSwimming();
    float getSpeed();

    int getEnchantmentSeed();

    boolean mayUseItemAt(BlockPos pos, Direction direction, ItemStack with);

    boolean setEntityOnShoulder(CompoundTag tag);
    void conduit_removeEntitiesOnShoulder();
    void conduit_respawnEntityOnShoulder(CompoundTag tag);

    float getStandingEyeHeight(Pose pose, EntityDimensions dimensions);
    float getAbsorptionAmount();
    void setAbsorptionAmount(float amount);
    void setRemainingFireTicks(int ticks);

    HumanoidArm getMainArm();

    CompoundTag conduit_getShoulderEntityLeft();
    CompoundTag conduit_getShoulderEntityRight();
    void conduit_setShoulderEntityLeft(CompoundTag tag);
    void conduit_setShoulderEntityRight(CompoundTag tag);

    ItemCooldowns getCooldowns();

    float getLuck();
    boolean canUseGameMasterBlocks();
}
