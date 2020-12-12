package systems.conduit.main.inventory;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.ServerPlayer;
import systems.conduit.main.inventory.events.InventoryEventHandler;
import systems.conduit.main.inventory.events.InventoryEventType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Innectic
 * @since 10/25/2020
 */
@RequiredArgsConstructor
public class CustomInventory {

    @NonNull private String name;
    @NonNull private MenuType<ChestMenu> menuType;
    @NonNull private int size;
    @NonNull private UUID uuid;

    @NonNull private Map<InventoryEventType, InventoryEventHandler> eventHandlers;
    @NonNull private Map<Integer, ItemStack> items;

    @Getter private Map<UUID, Integer> viewers = new HashMap<>();

    private Optional<ChestContainer> container = Optional.empty();

    public void setup() {
        container = Optional.of(ChestContainer.create(menuType, name));
    }

    public void open(ServerPlayer player) {
        container.ifPresent(c -> {
            AbstractContainerMenu menu = new ChestMenu(c.getType(), player.getContainerCounter(), ((Player) player).getInventory(), c, c.getContainerSize() / 9);
            menu.addSlotListener((net.minecraft.server.level.ServerPlayer) player);

            viewers.put(player.getUUID(), menu.containerId);
            items.forEach(menu::setItem);

            player.down().connection.send(new ClientboundOpenScreenPacket(menu.containerId, menu.getType(), new TextComponent(name)));
            player.down().containerMenu = menu;
            player.down().connection.send(new ClientboundContainerSetContentPacket(menu.containerId, player.down().containerMenu.getItems()));
        });
    }

    public void setItem(int position, ItemStack item, Optional<ServerPlayer> player) {
        Set<Map.Entry<UUID, Integer>> viewersToUpdate = player.map(p -> viewers.entrySet().stream().filter(entry -> entry.getKey() == p.getUUID()).collect(Collectors.toSet())).orElse(viewers.entrySet());

        // Modify the list in preparation to send to all the viewers that we're updating.
        Map<Integer, ItemStack> newItems = new HashMap<>(items);
        newItems.put(position, item);

        viewersToUpdate.forEach(entry -> {
            Optional<ServerPlayer> updating = Conduit.getPlayerManager().getPlayer(entry.getKey());
            if (!updating.isPresent() || viewers.containsValue(updating.get().down().containerMenu.containerId)) return;

            AbstractContainerMenu menu = updating.get().getContainerMenu();
            menu.setItem(position, item);

            updating.get().down().connection.send(new ClientboundContainerSetContentPacket(entry.getValue(), menu.getItems()));
        });
    }

    public void setItem(int position, ItemStack item) {
        setItem(position, item, Optional.empty());
    }

    public void pushEvent(InventoryEventType type, ServerPlayer player, ItemStack clicked, AbstractContainerMenu menu) {
        InventoryEventHandler handler = eventHandlers.getOrDefault(type, null);
        if (handler == null) return;

        handler.click(player, clicked, menu);
    }

    public void finished(int id) {
//        ids.removeIf(v -> v == id);
    }
}
