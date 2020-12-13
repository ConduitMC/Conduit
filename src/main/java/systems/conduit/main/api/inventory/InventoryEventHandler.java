package systems.conduit.main.api.inventory;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import systems.conduit.main.api.mixins.ServerPlayer;

/**
 * @author Innectic
 * @since 10/25/2020
 */
public interface InventoryEventHandler {

    void click(ServerPlayer player, ItemStack clicked, AbstractContainerMenu menu);
}
