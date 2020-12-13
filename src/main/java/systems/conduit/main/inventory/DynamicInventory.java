package systems.conduit.main.inventory;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import systems.conduit.main.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Innectic
 * @since 12/12/2020
 */
public class DynamicInventory extends StaticInventory {

    private Map<String, InventoryFrame> inventoryFrames = new HashMap<>();

    public DynamicInventory(Plugin plugin, MenuType<ChestMenu> menu, TextComponent title) {
        super(plugin, menu, title);
    }

    public void addFrame(String name, InventoryFrame frame) {
        inventoryFrames.put(name, frame);
    }

    public void removeFrame(String name) {
        inventoryFrames.remove(name);
    }
}
