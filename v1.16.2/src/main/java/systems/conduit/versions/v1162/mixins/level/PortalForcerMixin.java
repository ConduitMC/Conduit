package systems.conduit.versions.v1162.mixins.level;

import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.portal.PortalForcer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.events.types.WorldEvents;

import java.util.Optional;

/**
 * @author Innectic
 * @since 2/13/2021
 */
@Mixin(value = PortalForcer.class, remap = false)
public class PortalForcerMixin {

    @Inject(method = "createPortal", at = @At("HEAD"))
    public void createPortal(BlockPos blockPos, Direction.Axis axis, CallbackInfoReturnable<Optional<BlockUtil.FoundRectangle>> cir) {
        WorldEvents.PortalCreateEvent event = new WorldEvents.PortalCreateEvent(blockPos, axis);
        Conduit.getEventManager().dispatchEvent(event);
    }
}
