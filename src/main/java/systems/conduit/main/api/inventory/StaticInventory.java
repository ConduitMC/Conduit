package systems.conduit.main.api.inventory;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.mixins.ServerPlayer;
import systems.conduit.main.core.events.EventListener;
import systems.conduit.main.core.events.annotations.Listener;
import systems.conduit.main.core.events.types.PlayerEvents;
import systems.conduit.main.core.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Innectic
 * @since 12/11/2020
 */
public class StaticInventory implements EventListener {

    private final Map<UUID, String> viewers = new HashMap<>();

    private Map<String, InventoryFrame> frames = new HashMap<>();

    private ChestContainer container;
    private TextComponent title;

    public static final String DEFAULT_FRAME_NAME = "default";

    public StaticInventory(Plugin plugin, MenuType<ChestMenu> menu, TextComponent title) {
        container = ChestContainer.create(menu, title.getText());
        this.title = title;
        Conduit.getEventManager().registerEventClass(plugin, this);
    }

    public void addFrame(String name, InventoryFrame frame) {
        frames.put(name, frame);
    }

    public void open(ServerPlayer player) {
        // Get the default frame
        viewers.put(player.getUUID(), DEFAULT_FRAME_NAME);
        changeFrame(player, DEFAULT_FRAME_NAME);
    }

    public void close(ServerPlayer player) {
        InventoryFrame frame = frames.get(viewers.get(player.getUUID()));
        if (frame != null) frame.close(player);
        viewers.remove(player.getUUID());
    }

    public void changeFrame(ServerPlayer player, String frameName) {

        InventoryFrame newFrame = frames.get(frameName);
        if (newFrame == null) return;

        InventoryFrame oldFrame = frames.get(viewers.get(player.getUUID()));
        if (oldFrame != null) oldFrame.close(player);
        newFrame.open(player);
        viewers.put(player.getUUID(), frameName);
    }

    public Optional<InventoryFrame> getActiveFrame(ServerPlayer player) {
        return Optional.ofNullable(frames.getOrDefault(viewers.get(player.getUUID()), null));
    }

    public Optional<InventoryFrame> getFrame(String name) {
        return Optional.ofNullable(frames.getOrDefault(name, null));
    }

    @Listener
    public void onInventoryInteractEvent(PlayerEvents.InventoryClickEvent event) {
        ServerPlayer player = event.getPlayer();
        if (player == null) return;

        InventoryFrame viewedFrame = frames.get(viewers.get(player.getUUID()));
        if (viewedFrame == null) return;

        Optional<AbstractContainerMenu> inventoryMenu = viewedFrame.getMenu(player);
        if (!inventoryMenu.isPresent()) return;
        if (event.getContainer().containerId != inventoryMenu.get().containerId) return;

        Optional<InventoryButton> button = viewedFrame.findButtonBySlotId(player, event.getSlotId());
        if (!button.isPresent()) return;

        button.get().getConsumer().accept(player, event.getClickType());

        event.setCanceled(true);
    }

    @Listener
    public void onInventoryCloseEvent(PlayerEvents.InventoryCloseEvent event) {
        ServerPlayer player = event.getPlayer();
        if (player == null) return;

        InventoryFrame viewedFrame = frames.get(viewers.get(player.getUUID()));
        if (viewedFrame == null) return;

        Optional<AbstractContainerMenu> inventoryMenu = viewedFrame.getMenu(player);
        if (!inventoryMenu.isPresent()) return;
        if (event.getContainer().containerId != inventoryMenu.get().containerId) return;

        close(player);
    }

    public InventoryFrame newFrame() {
        return new InventoryFrame(this.container, this.title);
    }
}
