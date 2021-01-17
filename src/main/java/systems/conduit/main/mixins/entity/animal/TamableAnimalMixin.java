package systems.conduit.main.mixins.entity.animal;

import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.api.mixins.ServerPlayer;
import systems.conduit.main.core.events.types.EntityEvents;

/**
 * @author Innectic
 * @since 12/26/2020
 */
@Mixin(value = TamableAnimal.class, remap = false)
public class TamableAnimalMixin {

    @Inject(method = "tame", at = @At("HEAD"), cancellable = true)
    public void tame(Player player, CallbackInfo ci) {
        EntityEvents.EntityTameEvent event = new EntityEvents.EntityTameEvent((TamableAnimal) ((Object) this), (ServerPlayer) player);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) ci.cancel();
    }
}
