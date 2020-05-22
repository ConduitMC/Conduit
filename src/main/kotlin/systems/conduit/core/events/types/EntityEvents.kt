package systems.conduit.core.events.types

import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.AgableMob
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.animal.Sheep
import net.minecraft.world.entity.monster.Slime
import net.minecraft.world.level.block.SpawnerBlock
import systems.conduit.api.Entity
import systems.conduit.api.LivingEntity
import systems.conduit.core.events.Cancellable

/*
 * @author Innectic
 * @since 10/21/2019
 */
class EntityEvents {
    /**
     * This event is fired when an adult sheep consumes grass.
     * Implementation: [systems.conduit.core.mixins.event.entity.SheepMixin]
     */
    class SheepGrowWoolEvent(val sheep: Sheep): Cancellable()

    /**
     * This event is fired when a baby sheep eats something and progresses growth.
     * Implementation: [systems.conduit.core.mixins.event.entity.SheepMixin.ate]
     */
    class BabySheepEatEvent(val sheep: Sheep, val ageUpAmount: Int = 0): Cancellable()

    /**
     * This event is fired when a slime takes enough damage to split into more, smaller slimes.
     * Implementation: [systems.conduit.core.mixins.event.entity.SlimeMixin]
     */
    class SlimeSplitEvent(val parent: Slime, val children: List<Slime>): EventType()

    class EntityBreedEvent(val bred: Animal, val with: Animal, val child: AgableMob): Cancellable()

    class SpawnerSpawnEvent(val spawner: SpawnerBlock, val spawned: List<Entity>): Cancellable()

    /**
     * This event is fired when any entity in the server has an effect added to it.
     * Implementation: [systems.conduit.core.mixins.api.LivingEntityMixin.onEffectAdded]
     */
    class EffectAddedToEntityEvent(val entity: LivingEntity, val effect: MobEffectInstance): Cancellable()

    /**
     * This event is fired when any entity in the server has an effect removed from it.
     * Implementation: [systems.conduit.core.mixins.api.LivingEntityMixin.onEffectRemoved]
     */
    class EffectRemovedFromEntityEvent(val entity: LivingEntity, val effect: MobEffectInstance): Cancellable()
}