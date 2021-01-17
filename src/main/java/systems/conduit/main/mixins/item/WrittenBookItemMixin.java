package systems.conduit.main.mixins.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.WrittenBookItem;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.api.mixins.Level;
import systems.conduit.main.core.api.mixins.ServerPlayer;
import systems.conduit.main.core.events.types.WorldEvents;

/**
 * @author Innectic
 * @since 1/2/2021
 */
@Mixin(value = WrittenBookItem.class, remap = false)
public abstract class WrittenBookItemMixin {

    @Shadow public abstract InteractionResult useOn(UseOnContext useOnContext);

    @Inject(method = "useOn", at = @At(value = "HEAD", target = "Lnet/minecraft/world/level/block/LecternBlock;tryPlaceBook(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/item/ItemStack;)Z"), cancellable = true)
    public void useOn(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
        WorldEvents.LecternPlaceBookEvent event = new WorldEvents.LecternPlaceBookEvent(useOnContext.getClickedPos(), (Level) useOnContext.getLevel(), useOnContext.getItemInHand(), (ServerPlayer) useOnContext.getPlayer());
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            cir.setReturnValue(InteractionResult.PASS);
            cir.cancel();
        }
    }
}
