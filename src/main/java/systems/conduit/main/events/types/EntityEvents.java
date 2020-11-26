package systems.conduit.main.events.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.block.SpawnerBlock;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.api.LivingEntity;
import systems.conduit.main.events.Cancellable;

import java.util.List;

/*
 * @author Innectic
 * @since 10/21/2019
 */
public class EntityEvents {

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.event.entity.SheepMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class SheepGrowWoolEvent extends Cancellable {
        private Sheep sheep;
    }

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.event.entity.SheepMixin#ate()}
     */
    @AllArgsConstructor
    @Getter
    @Setter
    public static class BabySheepEatEvent extends Cancellable {
        private Sheep sheep;
        private int ageUpAmount;
    }

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.event.entity.SlimeMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class SlimeSplitEvent extends EventType {
        private Slime parent;
        private List<Slime> children;
    }

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.event.goal.BreedGoalMixin#breed(CallbackInfo)}
     */
    @AllArgsConstructor
    @Getter
    public static class EntityBreedEvent extends Cancellable {
        private Animal bred;
        private Animal with;
        private Animal child;
    }

    @AllArgsConstructor
    @Getter
    public static class SpawnerSpawnEvent extends Cancellable {
        private SpawnerBlock spawner;
        private List<Entity> spawned;
    }

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.api.LivingEntityMixin#onEffectAdded(MobEffectInstance, CallbackInfo)}
     */
    @AllArgsConstructor
    @Getter
    public static class EffectAddedToEntityEvent extends Cancellable {
        private LivingEntity entity;
        private MobEffectInstance effect;
    }

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.api.LivingEntityMixin#onEffectRemoved(MobEffectInstance, CallbackInfo)}
     */
    @AllArgsConstructor
    @Getter
    public static class EffectRemovedFromEntityEvent extends Cancellable {
        private LivingEntity entity;
        private MobEffectInstance effect;
    }
}
