package systems.conduit.main.mixins.player;

import net.minecraft.advancements.Advancement;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.events.types.PlayerEvents;

/**
 * @author Innectic
 * @since 1/2/2021
 */
@Mixin(value = PlayerAdvancements.class, remap = false)
public class PlayerAdvancementsMixin {

    @Shadow private ServerPlayer player;

    @Inject(method = "award", at = @At(value = "HEAD", target = "Lnet/minecraft/advancements/AdvancementRewards;grant(Lnet/minecraft/server/level/ServerPlayer;)V"), cancellable = true)
    public void award(Advancement advancement, String s, CallbackInfoReturnable<Boolean> cir) {
        PlayerEvents.AdvancementCompletedEvent event = new PlayerEvents.AdvancementCompletedEvent((systems.conduit.main.api.mixins.ServerPlayer) this.player, advancement);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "revoke", at = @At("HEAD"), cancellable = true)
    public void revoke(Advancement advancement, String s, CallbackInfoReturnable<Boolean> cir) {
        PlayerEvents.AdvancementRevokeEvent event = new PlayerEvents.AdvancementRevokeEvent((systems.conduit.main.api.mixins.ServerPlayer) this.player, advancement);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
