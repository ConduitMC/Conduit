package systems.conduit.main.mixins.api;

import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.events.types.EntityEvents;

import java.util.*;
import java.util.stream.Collectors;

@Mixin(value = Entity.class, remap = false)
public abstract class EntityMixin implements systems.conduit.main.api.Entity {

    @Shadow public Level level;

    @Shadow public abstract UUID getUUID();
    @Shadow public abstract void sendMessage(Component component, UUID uuid);

    @Override
    public void sendMessage(String message) {
        sendMessage(new TextComponent(message));
    }

    @Shadow public abstract void teleportTo(double v, double v1, double v2);

    @Override
    public Level getLevel() {
        return level;
    }

    @Shadow public abstract double getX();
    @Shadow public abstract double getY();
    @Shadow public abstract double getZ();

    @Override
    public void teleport(systems.conduit.main.api.Entity entity) {
        this.teleport(entity.getX(), entity.getY(), entity.getZ());
    }

    @Override
    public void teleport(Position position) {
        this.teleport(position.x(), position.y(), position.z());
    }

    @Override
    public void teleport(double x, double y, double z) {
        this.teleportTo(x, y, z);
    }

    @Shadow public abstract boolean isSpectator();
    @Shadow public abstract void unRide();
    @Shadow public abstract EntityType<?> getType();
    @Shadow public abstract int getId();
    @Shadow public abstract void setId(int id);
    @Shadow public abstract Set<String> getTags();
    @Shadow public abstract boolean addTag(String tag);
    @Shadow public abstract boolean removeTag(String tag);
    @Shadow public abstract void kill();
    @Shadow public abstract Pose getPose();
    @Shadow public abstract int getPortalWaitTime();
    @Shadow public abstract void setSecondsOnFire(int time);
    @Shadow public abstract void setRemainingFireTicks(int ticks);
    @Shadow public abstract int getRemainingFireTicks();
    @Shadow public abstract void clearFire();
    @Shadow public abstract boolean isSilent();
    @Shadow public abstract void setSilent(boolean silent);
    @Shadow public abstract boolean isNoGravity();
    @Shadow public abstract void setNoGravity(boolean gravity);
    @Shadow public abstract boolean isInWater();
    @Shadow public abstract boolean isInWaterOrRain();
    @Shadow public abstract boolean isInWaterRainOrBubble();
    @Shadow public abstract boolean isInWaterOrBubble();
    @Shadow public abstract boolean isUnderWater();
    //@Shadow public abstract boolean isUnderLiquid(Tag<Fluid> fluid);
    @Shadow public abstract boolean isInLava();
    @Shadow public abstract void moveRelative(float scale, Vec3 vector);
    @Shadow public abstract void setLevel(Level level);
    @Shadow public abstract void moveTo(double x, double y, double z, float xRot, float yRot);
    @Shadow public abstract float distanceTo(Entity entity);
    @Shadow public abstract double distanceToSqr(double x, double y, double z);
    @Shadow public abstract double distanceToSqr(Entity entity);
    @Shadow public abstract double distanceToSqr(Vec3 vector);
    @Shadow public abstract Vec3 getEyePosition(float scale);
    @Shadow public abstract boolean isAlive();
    @Shadow public abstract boolean isInWall();
    @Shadow public abstract boolean startRiding(Entity entity);
    @Shadow public abstract void stopRiding();
    @Shadow public abstract boolean isSwimming();
    @Shadow public abstract void setSwimming(boolean state);
    @Shadow public abstract boolean isSprinting();
    @Shadow public abstract void setSprinting(boolean state);
    @Shadow public abstract boolean isGlowing();
    @Shadow public abstract void setGlowing(boolean state);
    @Shadow public abstract boolean isInvisible();
    @Shadow public abstract Team shadow$getTeam();
    @Shadow public abstract boolean isAlliedTo(Team team);
    @Shadow public abstract boolean isAlliedTo(Entity entity);
    @Shadow public abstract void setInvisible(boolean state);
    @Shadow public abstract int getMaxAirSupply();
    @Shadow public abstract void setAirSupply(int supply);
    @Shadow public abstract Direction getDirection();
    @Shadow public abstract Entity shadow$getControllingPassenger();
    @Shadow public abstract List<Entity> shadow$getPassengers();
    @Shadow public abstract boolean hasPassenger(Entity entity);
    @Shadow public abstract boolean hasPassenger(Class<? extends Entity> entity);
    @Shadow public abstract Collection<Entity> getIndirectPassengers();
    @Shadow public abstract boolean hasOnePlayerPassenger();
    @Shadow public abstract boolean isPassengerOfSameVehicle(Entity entity);
    //@Shadow public abstract boolean hasIndirectPassenger(Entity entity);
    @Shadow public abstract Vec3 position();

    @Shadow protected abstract SoundEvent shadow$getSwimSound();
    @Shadow protected abstract SoundEvent shadow$getSwimSplashSound();
    @Shadow protected abstract SoundEvent shadow$getSwimHighSpeedSplashSound();

    public Optional<Team> conduit_getTeam() {
        return Optional.of(this.shadow$getTeam());
    }

    public SoundEvent conduit_getSwimSound() {
        return shadow$getSwimSound();
    }

    public SoundEvent conduit_getSwimSplashSound() {
        return shadow$getSwimSplashSound();
    }

    public SoundEvent conduit_getSwimHighSpeedSplashSound() {
        return shadow$getSwimHighSpeedSplashSound();
    }

    public List<systems.conduit.main.api.Entity> conduit_getPassengers() {
        return this.shadow$getPassengers().stream().map(systems.conduit.main.api.Entity.class::cast).collect(Collectors.toList());
    }

    public Optional<systems.conduit.main.api.Entity> conduit_getControllingPassenger() {
        Entity entity = this.shadow$getControllingPassenger();
        if (entity == null) return Optional.empty();
        return Optional.of((systems.conduit.main.api.Entity) entity);
    }

    @Inject(method = "changeDimension", at = @At("HEAD"))
    public void changeDimension(ServerLevel level, CallbackInfoReturnable<Entity> callback) {
        EntityEvents.LevelSwitchEvent event = new EntityEvents.LevelSwitchEvent((Entity) (Object) this, (ServerLevel) this.getLevel(), level);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) return;
    }
}
