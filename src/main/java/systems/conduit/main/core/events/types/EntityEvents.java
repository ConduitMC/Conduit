package systems.conduit.main.core.events.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.block.SpawnerBlock;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.api.mixins.LivingEntity;
import systems.conduit.main.core.events.Cancellable;

import java.util.List;

/*
 * @author Innectic
 * @since 10/21/2019
 */
public class EntityEvents {

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.entity.SheepMixin#ate()}
     */
    @AllArgsConstructor
    @Getter
    public static class SheepGrowWoolEvent extends Cancellable {
        private Sheep sheep;
    }

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.entity.SheepMixin#ate()}
     */
    @AllArgsConstructor
    @Getter
    @Setter
    public static class BabySheepEatEvent extends Cancellable {
        private Sheep sheep;
        private int ageUpAmount;
    }

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.entity.SlimeMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class SlimeSplitEvent extends EventType {
        private Slime parent;
        private List<Slime> children;
    }

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.goal.BreedGoalMixin#breed(CallbackInfo)}
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
     * Mixin implementation: {@link systems.conduit.main.mixins.entity.LivingEntityMixin#onEffectAdded(MobEffectInstance, CallbackInfo)}
     */
    @AllArgsConstructor
    @Getter
    public static class EffectAddedToEntityEvent extends Cancellable {
        private LivingEntity entity;
        private MobEffectInstance effect;
    }

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.entity.LivingEntityMixin#onEffectRemoved(MobEffectInstance, CallbackInfo)}
     */
    @AllArgsConstructor
    @Getter
    public static class EffectRemovedFromEntityEvent extends Cancellable {
        private LivingEntity entity;
        private MobEffectInstance effect;
    }

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.entity.EntityMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class LevelSwitchEvent extends Cancellable {
        private Entity entity;
        private ServerLevel current;
        private ServerLevel destination;
    }

    @AllArgsConstructor
    @Getter
    public static class CreeperChargeEvent extends Cancellable {
        private systems.conduit.main.api.mixins.Entity entity;
    }

    @AllArgsConstructor
    @Getter
    public static class VillagerZombieConversionEvent extends Cancellable {
        private systems.conduit.main.api.mixins.ServerLevel level;
        private Zombie zombie;
        private Villager villager;
    }

    @AllArgsConstructor
    @Getter
    public static class ZombieKillEntityEvent extends Cancellable {
        private systems.conduit.main.api.mixins.ServerLevel level;
        private Zombie zombie;
        private LivingEntity entity;
    }
}
