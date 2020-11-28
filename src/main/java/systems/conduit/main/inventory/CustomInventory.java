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
import systems.conduit.main.api.ServerPlayer;
import systems.conduit.main.inventory.events.InventoryEventHandler;
import systems.conduit.main.inventory.events.InventoryEventType;

import java.util.*;

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

    @Getter private List<Integer> ids = new ArrayList<>();

    private Optional<ChestContainer> container = Optional.empty();

    public void setup() {
        container = Optional.of(ChestContainer.create(menuType, name));
    }

    public void open(ServerPlayer player) {
        container.ifPresent(c -> {
            AbstractContainerMenu menu = new ChestMenu(c.getType(), player.getContainerCounter(), ((Player) player).getInventory(), c, c.getContainerSize() / 9);
            menu.addSlotListener((net.minecraft.server.level.ServerPlayer) player);

            ids.add(menu.containerId);
            items.forEach(menu::setItem);

            player.down().connection.send(new ClientboundOpenScreenPacket(menu.containerId, menu.getType(), new TextComponent(name)));
            player.down().containerMenu = menu;
            player.down().connection.send(new ClientboundContainerSetContentPacket(menu.containerId, player.down().containerMenu.getItems()));
        });
    }

    public void pushEvent(InventoryEventType type, ServerPlayer player, ItemStack clicked, AbstractContainerMenu menu) {
        InventoryEventHandler handler = eventHandlers.getOrDefault(type, null);
        if (handler == null) return;

        handler.click(player, clicked, menu);
    }

    public void finished(int id) {
        ids.removeIf(v -> v == id);
    }
}
