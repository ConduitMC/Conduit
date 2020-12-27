package systems.conduit.main.mixins.entity.animal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.animal.Pig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.events.types.EntityEvents;

/**
 * @author Innectic
 * @since 12/26/2020
 */
@Mixin(value = Pig.class, remap = false)
public class PigMixin {

    @Inject(method = "thunderHit", at = @At(value = "HEAD", target = "Lnet/minecraft/world/entity/EntityType;create(Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/entity/Entity;"), cancellable = true)
    public void thunderHit(ServerLevel level, LightningBolt bolt, CallbackInfo ci) {
        EntityEvents.PigConvertToPiglinEvent event = new EntityEvents.PigConvertToPiglinEvent((Pig) ((Object) this), (systems.conduit.main.api.mixins.ServerLevel) level);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) ci.cancel();
    }
}
