package systems.conduit.main.mixins.api.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Abilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author Innectic
 * @since 12/2/2020
 */
@Mixin(value = Abilities.class, remap = false)
public abstract class AbilitiesMixin implements systems.conduit.main.api.player.Abilities {

    @Shadow public abstract void addSaveData(CompoundTag tag);
    @Shadow public abstract void loadSaveData(CompoundTag tag);

    @Shadow public abstract float getFlyingSpeed();
    @Shadow public abstract float getWalkingSpeed();

    @Shadow public boolean invulnerable;
    @Shadow public boolean mayfly;
    @Shadow public boolean flying;
    @Shadow public boolean instabuild;
    @Shadow public boolean mayBuild;
    @Shadow private float flyingSpeed;
    @Shadow private float walkingSpeed;

    @Override
    public boolean isInvulnerable() {
        return this.invulnerable;
    }

    @Override
    public boolean mayFly() {
        return this.mayfly;
    }

    @Override
    public boolean isFlying() {
        return this.flying;
    }

    @Override
    public boolean canInstaBuild() {
        return this.instabuild;
    }

    @Override
    public boolean mayBuild() {
        return this.mayBuild;
    }

    @Override
    public void setFlyingSpeed(float speed){
        this.flyingSpeed = speed;
    }

    @Override
    public void setWalkingSpeed(float speed) {
        this.walkingSpeed = speed;
    }

    @Override
    public void setInvulnerable(boolean invulnerable) {
        this.invulnerable = invulnerable;
    }

    @Override
    public void setMayFly(boolean mayFly) {
        this.mayfly = mayFly;
    }

    @Override
    public void setIsFlying(boolean isFlying) {
        this.flying = isFlying;
    }

    @Override
    public void setInstaBuild(boolean instabuild) {
        this.instabuild = instabuild;
    }

    @Override
    public void setMayBuild(boolean mayBuild) {
        this.mayBuild = mayBuild;
    }
}
