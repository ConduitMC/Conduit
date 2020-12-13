package systems.conduit.main.api.mixins.player;

import net.minecraft.nbt.CompoundTag;

/**
 * @author Innectic
 * @since 12/2/2020
 */
public interface Abilities {

    void addSaveData(CompoundTag tag);
    void loadSaveData(CompoundTag tag);

    float getFlyingSpeed();
    float getWalkingSpeed();
    boolean isInvulnerable();
    boolean mayFly();
    boolean isFlying();
    boolean canInstaBuild();
    boolean mayBuild();

    void setFlyingSpeed(float speed);
    void setWalkingSpeed(float speed);
    void setInvulnerable(boolean invulnerable);
    void setMayFly(boolean mayFly);
    void setIsFlying(boolean isFlying);
    void setInstaBuild(boolean instaBuild);
    void setMayBuild(boolean mayBuild);
}
