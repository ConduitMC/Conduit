package systems.conduit.main.mixins.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.ServerPlayer;
import systems.conduit.main.events.types.PlayerEvents;

/**
 * @author Innectic
 * @since 12/11/2020
 */
@Mixin(value = AbstractContainerMenu.class, remap = false)
public class AbstractContainerMenuMixin {

    @Inject(method = "doClick", at = @At("HEAD"), cancellable = true)
    public void doClick(int slotId, int quickcraftHeader, ClickType clickType, Player p, CallbackInfoReturnable<ItemStack> cir) {
        ServerPlayer player = (ServerPlayer) p;
        PlayerEvents.InventoryClickEvent event = new PlayerEvents.InventoryClickEvent(player, (AbstractContainerMenu) (Object) this, clickType, slotId);
        Conduit.getEventManager().dispatchEvent(event);

//        if (event.isCanceled()) {
//            AbstractContainerMenu inventory = player.getContainerMenu();
//            if (inventory == null) return;
//
//            inventory.setItem(slotId, inventory.getSlot(slotId).getItem());
//
//            cir.setReturnValue(ItemStack.EMPTY);
//            cir.cancel();
//        }
    }

    @Inject(method = "removed", at = @At("HEAD"))
    public void removed(Player player, CallbackInfo ci) {
        PlayerEvents.InventoryCloseEvent event = new PlayerEvents.InventoryCloseEvent((ServerPlayer) player, (AbstractContainerMenu) (Object) this);
        Conduit.getEventManager().dispatchEvent(event);
    }
}
