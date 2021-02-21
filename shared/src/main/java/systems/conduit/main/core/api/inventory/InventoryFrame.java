package systems.conduit.main.core.api.inventory;

import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.api.mixins.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author Innectic
 * @since 12/12/2020
 */
@RequiredArgsConstructor
public class InventoryFrame implements GUI {

    private final Map<Integer, InventoryButton> buttons = new HashMap<>();

    private Map<UUID, Map<Integer, InventoryButton>> layerData = new HashMap<>();
    private Map<UUID, AbstractContainerMenu> viewers = new HashMap<>();

    private final ChestContainer container;
    private final Component title;

    public final InventoryFrame addDefaultButton(BiConsumer<ServerPlayer, ClickType> consumer, ItemStack item, int slot)  {
        this.buttons.put(slot, new InventoryButton(item, consumer));
        viewers.keySet().stream().map(Conduit.getPlayerManager()::getPlayer).filter(Optional::isPresent).map(Optional::get).forEach(this::updateFrame);
        return this;
    }

    public void updateFrame(ServerPlayer player) {
        AbstractContainerMenu containerMenu = viewers.get(player.getUUID());
        // Add the default buttons
        buttons.forEach((slot, button) -> containerMenu.setItem(slot, button.getItem()));
        // Add the overrides for a custom layer
        layerData.getOrDefault(player.getUUID(), new HashMap<>()).forEach((slot, button) -> containerMenu.setItem(slot, button.getItem()));

        player.down().connection.send(new ClientboundContainerSetContentPacket(containerMenu.containerId, player.down().containerMenu.getItems()));
    }

    public void addLayeredButton(ServerPlayer player, BiConsumer<ServerPlayer, ClickType> consumer, int slot, ItemStack item) {
        Map<Integer, InventoryButton> layerButtons = layerData.getOrDefault(player.getUUID(), new HashMap<>());
        layerButtons.put(slot, new InventoryButton(item, consumer));
        layerData.put(player.getUUID(), layerButtons);
        viewers.forEach((uuid, menu) -> Conduit.getPlayerManager().getPlayer(uuid).ifPresent(this::updateFrame));
    }

    public void removeLayeredButton(ServerPlayer player, int slot) {
        if (layerData.containsKey(player.getUUID())) layerData.get(player.getUUID()).remove(slot);
        viewers.forEach((uuid, menu) -> Conduit.getPlayerManager().getPlayer(uuid).ifPresent(this::updateFrame));
    }

    @Override
    public void open(ServerPlayer player) {
        AbstractContainerMenu containerMenu = new ChestMenu(container.getType(), player.getContainerCounter(), player.getInventory(), container, container.getContainerSize() / 9);
        containerMenu.addSlotListener(player.down());

        viewers.put(player.getUUID(), containerMenu);

        player.down().connection.send(new ClientboundOpenScreenPacket(containerMenu.containerId, containerMenu.getType(), title));
        player.down().containerMenu = containerMenu;

        updateFrame(player);
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

    public void clearSlot(int slot) {
        buttons.remove(slot);
        viewers.forEach((uuid, menu) -> Conduit.getPlayerManager().getPlayer(uuid).ifPresent(this::updateFrame));
    }

    @Override
    public ImmutableList<ServerPlayer> getActiveViewers() {
        return ImmutableList.copyOf(viewers.keySet().stream().map(Conduit.getPlayerManager()::getPlayer)
                .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList()));
    }

    public final Optional<InventoryButton> findButtonBySlotId(ServerPlayer player, int slot) {
        if (layerData.containsKey(player.getUUID()) && layerData.get(player.getUUID()).containsKey(slot)) return Optional.of(layerData.get(player.getUUID()).get(slot));
        return Optional.ofNullable(buttons.getOrDefault(slot, null));
    }

    public Optional<AbstractContainerMenu> getMenu(ServerPlayer player) {
        return Optional.ofNullable(viewers.getOrDefault(player.getUUID(), null));
    }
}
