package systems.conduit.core.mixins.event.entity

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.animal.Animal
import net.minecraft.world.entity.animal.Sheep
import net.minecraft.world.level.Level
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Overwrite
import systems.conduit.core.Conduit
import systems.conduit.core.events.types.EntityEvents

/*
 * @author Innectic
 * @since 10/21/2019
 */
@Mixin(value = [Sheep::class], remap = false)
abstract class SheepMixin protected constructor(entityType: EntityType<out Animal?>?, level: Level?): Animal(entityType, level) {
    /**
     * Sheep eat and sheared event registering.
     *
     * @author ConduitMC
     */
    @Suppress("CAST_NEVER_SUCCEEDS")
    @Overwrite
    override fun ate() {
        val sheep: Sheep = this as Sheep
        if (this.isBaby()) {
            // Submit the baby event
            val event = EntityEvents.BabySheepEatEvent(sheep, 60)
            Conduit.eventManager.dispatchEvent(event)
            // If this was cancelled, then we'll just set the age up to 0.
            val ageUp = if (event.cancelled) 0 else event.ageUpAmount
            // Now that the event has been dispatched, we can apply the age up amount.
            sheep.ageUp(ageUp)
            return
        }
        // Since this is not a baby, then this is a regular sheep eat grass event.
        val event = EntityEvents.SheepGrowWoolEvent(sheep)
        Conduit.eventManager.dispatchEvent(event)

        // If this event was cancelled, then we will leave the sheep sheared.
        sheep.isSheared = event.cancelled
    }
}
