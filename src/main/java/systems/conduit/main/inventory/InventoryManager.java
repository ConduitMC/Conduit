package systems.conduit.main.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Innectic
 * @since 10/25/2020
 */
public class InventoryManager {

    private Map<UUID, CustomInventory> inventories = new HashMap<>();

    public Optional<CustomInventory> findInventoryByPlayerContainerId(int id) {
        return null;
//        return inventories.values().stream().filter(inv -> inv.getIds().contains(id)).findFirst();
    }

    public void register(UUID uuid, CustomInventory inventory) {
        inventories.put(uuid, inventory);
    }

    public void remove(UUID uuid) {
        inventories.remove(uuid);
    }
}
