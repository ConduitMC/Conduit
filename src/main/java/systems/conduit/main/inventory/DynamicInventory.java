package systems.conduit.main.inventory;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import systems.conduit.main.api.ServerPlayer;
import systems.conduit.main.plugin.Plugin;

import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * @author Innectic
 * @since 12/12/2020
 */
public class DynamicInventory extends StaticInventory {

    public DynamicInventory(Plugin plugin, MenuType<ChestMenu> menu, TextComponent title) {
        super(plugin, menu, title);
    }

    public final void addButton(InventoryButton button, int slot, UUID uuid) {
        buttons.put(slot, button);
        updateInventory();
    }

    public final void addButton(BiConsumer<ServerPlayer, ClickType> consumer, ItemStack item, int slot, UUID uuid) {
        buttons.put(slot, new InventoryButton(item, consumer));
        updateInventory();
    }

    public final void clearButton(InventoryButton button, UUID uuid) {
        findSlotByButton(button).ifPresent(this::clearSlot);
        updateInventory();
    }

    public final void clearSlot(int slot, UUID uuid) {
        buttons.remove(slot);
        updateInventory();
    }
}
