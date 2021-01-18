package systems.conduit.main.mixins.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.api.mixins.ServerPlayer;
import systems.conduit.main.core.events.types.PlayerEvents;

/**
 * @author Innectic
 * @since 12/11/2020
 */
@Mixin(value = AbstractContainerMenu.class, remap = false)
public class AbstractContainerMenuMixin {

    @Inject(method = "removed", at = @At("HEAD"))
    public void removed(Player player, CallbackInfo ci) {
        PlayerEvents.InventoryCloseEvent event = new PlayerEvents.InventoryCloseEvent((ServerPlayer) player, (AbstractContainerMenu) (Object) this);
        Conduit.getEventManager().dispatchEvent(event);
    }
}
