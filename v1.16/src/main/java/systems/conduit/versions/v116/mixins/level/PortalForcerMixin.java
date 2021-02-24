package systems.conduit.versions.v116.mixins.level;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.PortalForcer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.events.types.WorldEvents;

/**
 * @author Innectic
 * @since 2/13/2021
 */
@Mixin(value = PortalForcer.class, remap = false)
public class PortalForcerMixin {

    @Inject(method = "createPortal", at = @At("HEAD"))
    public void createPortal(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        WorldEvents.PortalCreateEvent event = new WorldEvents.PortalCreateEvent(entity.blockPosition(), Direction.Axis.X);
        Conduit.getEventManager().dispatchEvent(event);
    }
}
