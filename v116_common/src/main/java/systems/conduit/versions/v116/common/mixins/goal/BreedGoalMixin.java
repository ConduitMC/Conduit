package systems.conduit.versions.v116.common.mixins.goal;

import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.events.types.EntityEvents;

/*
 * @author Innectic
 * @since 10/23/2019
 */
@Mixin(value = BreedGoal.class, remap = false)
public class BreedGoalMixin {

    @Shadow @Final protected Animal animal;
    @Shadow protected Animal partner;

    @Inject(method = "breed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelWriter;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    public void breed(CallbackInfo ci) {
        EntityEvents.EntityBreedEvent event = new EntityEvents.EntityBreedEvent(animal, partner, null);
        Conduit.getEventManager().dispatchEvent(event);
    }
}
