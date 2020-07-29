package systems.conduit.main.mixins.event.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import systems.conduit.main.Conduit;
import systems.conduit.main.events.types.EntityEvents;

/*
 * @author Innectic
 * @since 10/21/2019
 */
@Mixin(value = Sheep.class, remap = false)
public abstract class SheepMixin extends Animal {

    protected SheepMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Sheep eat and sheared event registering.
     *
     * @author ConduitMC
     */
    @Overwrite
    public void ate() {
        Sheep sheep = (Sheep) ((Object) this);

        if (this.isBaby()) {
            // Submit the baby event
            EntityEvents.BabySheepEatEvent event = new EntityEvents.BabySheepEatEvent(sheep, 60);
            Conduit.getEventManager().dispatchEvent(event);
            // If this was cancelled, then we'll just set the age up to 0.
            int ageUp = event.isCanceled() ? 0 : event.getAgeUpAmount();
            // Now that the event has been dispatched, we can apply the age up amount.
            sheep.ageUp(ageUp);
            return;
        }
        // Since this is not a baby, then this is a regular sheep eat grass event.
        EntityEvents.SheepGrowWoolEvent event = new EntityEvents.SheepGrowWoolEvent(sheep);
        Conduit.getEventManager().dispatchEvent(event);

        // If this event was cancelled, then we will leave the sheep sheared.
        sheep.setSheared(event.isCanceled());
    }
}
