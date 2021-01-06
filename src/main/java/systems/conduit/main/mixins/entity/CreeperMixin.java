package systems.conduit.main.mixins.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.api.mixins.Entity;
import systems.conduit.main.core.events.types.EntityEvents;

/**
 * @author Innectic
 * @since 12/26/2020
 */
@Mixin(value = Creeper.class, remap = false)
public class CreeperMixin {

    @Inject(method = "thunderHit", at = @At("HEAD"), cancellable = true)
    public void thunderHit(ServerLevel serverLevel, LightningBolt lightningBolt, CallbackInfo ci) {
        EntityEvents.CreeperChargeEvent event = new EntityEvents.CreeperChargeEvent((Entity) this);
        Conduit.getEventManager().dispatchEvent(event);

        // Check if the event has been cancelled
        if (event.isCanceled()) ci.cancel();
    }
}
