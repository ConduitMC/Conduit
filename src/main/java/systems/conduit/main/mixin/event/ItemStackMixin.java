package systems.conduit.main.mixin.event;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.Player;
import systems.conduit.main.events.EventType;

@Mixin(value = ItemStack.class, remap = false)
public class ItemStackMixin {

    @Inject(method = "useOn", at = @At("HEAD"))
    private void useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        EventType.BlockInteractEvent event = new EventType.BlockInteractEvent((Player) context.getPlayer(),
                context.getLevel().getBlockState(context.getClickedPos()), context.getHand(), context.getItemInHand(),
                context.getClickedFace(), context.isSneaking(), context.isInside());

        Conduit.getEventManager().dispatchEvent(event);
    }
}
