package systems.conduit.main.inventory;

import com.google.common.collect.ImmutableList;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.ServerPlayer;
import systems.conduit.main.events.EventListener;
import systems.conduit.main.events.annotations.Listener;
import systems.conduit.main.events.types.PlayerEvents;
import systems.conduit.main.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;

/**
 * @author Innectic
 * @since 12/11/2020
 */
public class StaticInventory implements EventListener, GUI {

    private final Map<UUID, AbstractContainerMenu> viewers = new HashMap<>();

    private final Map<Integer, InventoryButton> buttons = new HashMap<>();

    private ChestContainer container;
    private TextComponent title;

    public StaticInventory(Plugin plugin, MenuType<ChestMenu> menu, TextComponent title) {
        container = ChestContainer.create(menu, title.getText());
        this.title = title;
        Conduit.getEventManager().registerEventClass(plugin, this);
    }

    @Override
    public void open(ServerPlayer player) {
        AbstractContainerMenu containerMenu = new ChestMenu(container.getType(), player.getContainerCounter(),
                player.getInventory(), container, container.getContainerSize() / 9);
        containerMenu.addSlotListener(player.down());

        viewers.put(player.getUUID(), containerMenu);

        buttons.forEach((slot, button) -> containerMenu.setItem(slot, button.getItem()));

        player.down().connection.send(new ClientboundOpenScreenPacket(containerMenu.containerId, containerMenu.getType(), title));
        player.down().containerMenu = containerMenu;

        player.down().connection.send(new ClientboundContainerSetContentPacket(containerMenu.containerId, player.down().containerMenu.getItems()));
    }


    @Override
    public void open(Iterable<ServerPlayer> players) {
        players.forEach(this::open);
    }

    @Override
    public void close(ServerPlayer player) {
        this.viewers.remove(player.getUUID());
    }

    @Override
    public void close(Iterable<ServerPlayer> players) {
        players.forEach(this::close);
    }

    @Override
    public ImmutableList<ServerPlayer> getActiveViewers() {
        return null;
    }

    public final void updateInventory() {
        viewers.forEach((p, container) -> Conduit.getPlayerManager().getPlayer(p).ifPresent(player ->
                player.down().connection.send(new ClientboundContainerSetContentPacket(container.containerId, player.down().containerMenu.getItems()))));
    }

    public final void addButton(InventoryButton button, int slot) {
        buttons.put(slot, button);
        updateInventory();
    }

    public final void addButton(BiConsumer<ServerPlayer, ClickType> consumer, ItemStack item, int slot) {
        buttons.put(slot, new InventoryButton(item, consumer));
        updateInventory();
    }

    public final void clearButton(InventoryButton button) {
        findSlotByButton(button).ifPresent(this::clearSlot);
        updateInventory();
    }

    public final void clearSlot(int slot) {
        buttons.remove(slot);
        updateInventory();
    }

    private Optional<Integer> findSlotByButton(InventoryButton button) {
        return buttons.entrySet().stream().filter(entry -> entry.getValue().equals(button)).map(Map.Entry::getKey).findFirst();
    }

    @Listener
    public void onInventoryInteractEvent(PlayerEvents.InventoryClickEvent event) {
        AbstractContainerMenu inventoryMenu = viewers.getOrDefault(event.getPlayer().getUUID(), null);
        if (inventoryMenu == null) return;
        if (event.getContainer().containerId != inventoryMenu.containerId) return;

        InventoryButton button = buttons.get(event.getSlotId());
        if (button == null) return;

        button.getConsumer().accept(event.getPlayer(), event.getClickType());

        event.setCanceled(true);
    }

    @Listener
    public void onInventoryCloseEvent(PlayerEvents.InventoryCloseEvent event) {
        if (event.getPlayer() == null || event.getContainer() == null) return;
        AbstractContainerMenu inventoryMenu = viewers.getOrDefault(event.getPlayer().getUUID(), null);
        if (inventoryMenu == null) return;
        if (event.getContainer().containerId != inventoryMenu.containerId) return;

        this.viewers.remove(event.getPlayer().getUUID());
    }
}
