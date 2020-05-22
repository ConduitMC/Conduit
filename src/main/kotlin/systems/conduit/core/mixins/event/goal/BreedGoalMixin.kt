package systems.conduit.core.mixins.event.goal

import net.minecraft.world.entity.ai.goal.BreedGoal
import net.minecraft.world.entity.animal.Animal
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import systems.conduit.core.Conduit
import systems.conduit.core.events.types.EntityEvents

/*
 * @author Innectic
 * @since 10/23/2019
 */
@Mixin(value = [BreedGoal::class], remap = false)
open class BreedGoalMixin {

    @Shadow @Final protected var animal: Animal? = null

    @Shadow protected var partner: Animal? = null

    @Inject(method = ["breed"], at = [At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelWriter;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z")])
    fun breed(ci: CallbackInfo?) {
        val offSpring = this.animal!!.getBreedOffspring(this.partner)
        if (animal != null && partner != null && offSpring != null) {
            val event = EntityEvents.EntityBreedEvent(animal!!, partner!!, offSpring)
            Conduit.eventManager.dispatchEvent(event)
        }
    }
}
