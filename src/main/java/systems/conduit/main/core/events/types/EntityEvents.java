package systems.conduit.main.core.events.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.phys.Vec3;
import systems.conduit.main.core.api.mixins.Entity;
import systems.conduit.main.core.api.mixins.LivingEntity;
import systems.conduit.main.core.api.mixins.ServerLevel;
import systems.conduit.main.core.api.mixins.ServerPlayer;
import systems.conduit.main.core.events.Cancellable;

/*
 * @author Innectic
 * @since 10/21/2019
 */
public class EntityEvents {

    @AllArgsConstructor
    @Getter
    public static class SheepGrowWoolEvent extends Cancellable {
        private Sheep sheep;
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class BabySheepEatEvent extends Cancellable {
        private Sheep sheep;
        private int ageUpAmount;
    }

    @AllArgsConstructor
    @Getter
    public static class EntityBreedEvent extends Cancellable {
        private Animal bred;
        private Animal with;
        private Animal child;
    }

    @AllArgsConstructor
    @Getter
    public static class EffectAddedToEntityEvent extends Cancellable {
        private LivingEntity entity;
        private MobEffectInstance effect;
    }

    @AllArgsConstructor
    @Getter
    public static class EffectRemovedFromEntityEvent extends Cancellable {
        private LivingEntity entity;
        private MobEffectInstance effect;
    }

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
        private systems.conduit.main.core.api.mixins.Entity entity;
    }

    @AllArgsConstructor
    @Getter
    public static class PigConvertToPiglinEvent extends Cancellable {
        private Pig pig;
        private ServerLevel level;
    }

    @AllArgsConstructor
    @Getter
    public static class VillagerZombieConversionEvent extends Cancellable {
        private ServerLevel level;
        private Zombie zombie;
        private Villager villager;
    }

    @AllArgsConstructor
    @Getter
    public static class ZombieKillEntityEvent extends Cancellable {
        private ServerLevel level;
        private Zombie zombie;
        private LivingEntity entity;
    }

    @AllArgsConstructor
    @Getter
    public static class EntityTameEvent extends Cancellable {
        private TamableAnimal animal;
        private ServerPlayer player;
    }

    @AllArgsConstructor
    @Getter
    public static class SheepSetColorEvent extends Cancellable {
        private Sheep sheep;
        private DyeColor color;
    }

    @AllArgsConstructor
    @Getter
    public static class MoveEvent extends Cancellable {
        private Entity entity;
        private Vec3 to;
        private Vec3 from;
        private MoverType mover;
    }
}
