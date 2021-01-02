package systems.conduit.main.mixins.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.mixins.ServerPlayer;
import systems.conduit.main.core.events.types.PlayerEvents;
import systems.conduit.main.core.events.types.WorldEvents;

import java.util.function.Consumer;

@Mixin(value = ItemStack.class, remap = false)
public class ItemStackMixin {

    @Inject(method = "useOn", at = @At("HEAD"))
    private void useOn(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        boolean isCrouching = (context.getPlayer() != null && context.getPlayer().getPose() != null && context.getPlayer().getPose() == Pose.values()[5]);
        WorldEvents.BlockInteractEvent event = new WorldEvents.BlockInteractEvent((ServerPlayer) context.getPlayer(),
                context.getLevel().getBlockState(context.getClickedPos()), context.getHand(), context.getItemInHand(),
                context.getClickedFace(), isCrouching, context.isInside());
        Conduit.getEventManager().dispatchEvent(event);
    }

    @Inject(method = "hurtAndBreak", at = @At(value = "HEAD", target = "Lnet/minecraft/world/item/ItemStack;hurt(ILjava/util/Random;Lnet/minecraft/server/level/ServerPlayer;)Z"), cancellable = true)
    public void hurtAndBreak(int i, LivingEntity t, Consumer<? extends LivingEntity> consumer, CallbackInfo ci) {
        if (t instanceof ServerPlayer) {
            PlayerEvents.ItemBreakEvent event = new PlayerEvents.ItemBreakEvent((ServerPlayer) t, (ItemStack) ((Object) this));
            Conduit.getEventManager().dispatchEvent(event);

            if (event.isCanceled()) ci.cancel();
        }
    }
}
