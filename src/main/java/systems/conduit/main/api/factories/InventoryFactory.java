package systems.conduit.main.api.factories;

import lombok.Builder;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import systems.conduit.main.Conduit;
import systems.conduit.main.inventory.CustomInventory;
import systems.conduit.main.inventory.events.InventoryEventHandler;
import systems.conduit.main.inventory.events.InventoryEventType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Innectic
 * @since 10/25/2020
 */
@Builder
public class InventoryFactory {

    @Builder.Default private String name = "Inventory";
    @Builder.Default private MenuType<ChestMenu> menuType = MenuType.GENERIC_9x1;
    @Builder.Default private int size = 9;

    private final Map<InventoryEventType, InventoryEventHandler> eventHandlers = new HashMap<>();
    private final Map<Integer, ItemStack> items = new HashMap<>();

    public InventoryFactory rightClick(InventoryEventHandler handler) {
        eventHandlers.put(InventoryEventType.RIGHT_CLICK, handler);
        return this;
    }

    public InventoryFactory leftClick(InventoryEventHandler handler) {
        eventHandlers.put(InventoryEventType.LEFT_CLICK, handler);
        return this;
    }

    public InventoryFactory middleClick(InventoryEventHandler handler) {
        eventHandlers.put(InventoryEventType.MIDDLE_CLICK, handler);
        return this;
    }

    public InventoryFactory set(int i, ItemStack item) {
        items.put(i, item);
        return this;
    }

    public CustomInventory register() {
        UUID uuid = UUID.randomUUID();
        CustomInventory inv = new CustomInventory(name, menuType, size, uuid, eventHandlers, items);
        inv.setup();

        Conduit.getInventoryManager().register(uuid, inv);

        return inv;
    }
}
