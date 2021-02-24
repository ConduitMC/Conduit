package systems.conduit.versions.v116.common.mixins.player;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Abilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * @author Innectic
 * @since 12/2/2020
 */
@Mixin(value = Abilities.class, remap = false)
public abstract class AbilitiesMixin implements systems.conduit.main.core.api.mixins.player.Abilities {

    @Shadow public abstract void addSaveData(CompoundTag tag);
    @Shadow public abstract void loadSaveData(CompoundTag tag);

    @Shadow public abstract float getFlyingSpeed();
    @Shadow public abstract float getWalkingSpeed();

    @Shadow public boolean mayfly;
    @Shadow public boolean instabuild;
    @Shadow public boolean mayBuild;

    @Accessor public abstract boolean isInvulnerable();
    @Accessor public abstract boolean isFlying();

    @Accessor public abstract void setInvulnerable(boolean invulnerable);
    @Accessor public abstract void setFlying(boolean flying);
    @Accessor public abstract void setInstabuild(boolean instabuild);
    @Accessor public abstract void setMayBuild(boolean mayBuild);
    @Accessor public abstract void setFlyingSpeed(float flyingSpeed);
    @Accessor public abstract void setWalkingSpeed(float walkingSpeed);

    @Override
    public boolean mayFly() {
        return this.mayfly;
    }

    @Override
    public void setMayFly(boolean mayFly) {
        this.mayfly = mayFly;
    }

    @Override
    public boolean canInstaBuild() {
        return this.instabuild;
    }

    @Override
    public boolean mayBuild() {
        return mayBuild;
    }
}
