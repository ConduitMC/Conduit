package systems.conduit.main.api;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;

import java.util.*;
import java.util.function.Predicate;

/**
 * Base Conduit interface for use with mixins.
 * Implementation: {@link systems.conduit.main.mixins.api.EntityMixin}
 *
 * @since API 0.1
 */
public interface Entity {

    UUID getUUID();

    double getX();
    double getY();
    double getZ();

    void sendMessage(String message);
    void sendMessage(Component message);

    Level getLevel();

    void teleport(double x, double y, double z);
    void teleport(Position position);
    void teleport(Entity entity);

    boolean isSpectator();
    void unRide();
    EntityType<?> getType();
    int getId();
    void setId(int id);
    Set<String> getTags();
    boolean addTag(String tag);
    boolean removeTag(String tag);
    void kill();
    Pose getPose();
    int getPortalWaitTime();
    void setSecondsOnFire(int time);
    void setRemainingFireTicks(int ticks);
    int getRemainingFireTicks();
    void clearFire();
    boolean isSilent();
    void setSilent(boolean silent);
    boolean isNoGravity();
    void setNoGravity(boolean gravity);
    boolean isInWater();
    boolean isInWaterOrRain();
    boolean isInWaterRainOrBubble();
    boolean isInWaterOrBubble();
    boolean isUnderWater();
    boolean isUnderLiquid(Tag<Fluid> fluid);
    boolean isInLava();
    void moveRelative(float scale, Vec3 vector);
    void moveTo(double x, double y, double z, float xRot, float yRot);
    float distanceTo(Entity entity);
    double distanceToSqr(double x, double y, double z);
    double distanceToSqr(Entity entity);
    double distanceToSqr(Vec3 vector);
    Vec3 getEyePosition(float scale);
    boolean isAlive();
    boolean isInWall();
    boolean startRiding(Entity entity);
    void stopRiding();
    boolean isSwimming();
    void setSwimming(boolean state);
    boolean isSprinting();
    void setSprinting(boolean state);
    boolean isGlowing();
    void setGlowing(boolean state);
    boolean isInvisible();
    Optional<Team> conduit_getTeam();
    boolean isAlliedTo(Team team);
    boolean isAlliedTo(Entity entity);
    void setInvisible(boolean state);
    int getMaxAirSupply();
    void setAirSupply(int supply);
    Direction getDirection();
    Optional<Entity> conduit_getControllingPassenger();
    List<Entity> conduit_getPassengers();
    boolean hasPassenger(Entity entity);
    boolean hasPassenger(Predicate<net.minecraft.world.entity.Entity> entity);  // TODO: Convert to Conduit entity
    Iterable<net.minecraft.world.entity.Entity> getIndirectPassengers();  // TODO: Convert to Conduit entity
    boolean hasExactlyOnePlayerPassenger();
    boolean isPassengerOfSameVehicle(Entity entity);
    Vec3 position();

    SoundEvent conduit_getSwimSound();
    SoundEvent conduit_getSwimSplashSound();
    SoundEvent conduit_getSwimHighSpeedSplashSound();
}
