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

    public static int getSizeFromEnum(MenuType<ChestMenu> size) {
        if (MenuType.GENERIC_9x2.equals(size)) return 18;
        else if (MenuType.GENERIC_9x3.equals(size)) return 27;
        else if (MenuType.GENERIC_9x4.equals(size)) return 36;
        else if (MenuType.GENERIC_9x5.equals(size)) return 45;
        else if (MenuType.GENERIC_9x6.equals(size)) return 54;
        return 9;
    }

    public CustomInventory register() {
        UUID uuid = UUID.randomUUID();
        CustomInventory inv = new CustomInventory(name, menuType, getSizeFromEnum(menuType), uuid, eventHandlers, items);
        inv.setup();

        Conduit.getInventoryManager().register(uuid, inv);

        return inv;
    }
}
