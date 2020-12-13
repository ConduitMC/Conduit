package systems.conduit.main.mixins;

import it.unimi.dsi.fastutil.ints.Int2ShortMap;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundContainerAckPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import systems.conduit.main.Conduit;
import systems.conduit.main.events.types.PlayerEvents;

import java.util.UUID;
import java.util.stream.Collectors;

@Mixin(value = ServerGamePacketListenerImpl.class, remap = false)
public class ServerGamePacketListenerMixin {

    @Shadow public ServerPlayer player;

    @Shadow @Final private Int2ShortMap expectedAcks;

    @Redirect(method = "handleChat(Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"))
    private void handleChat(PlayerList playerList, Component component, ChatType chatType, UUID uuid) {
        PlayerEvents.PlayerChatEvent event = new PlayerEvents.PlayerChatEvent((systems.conduit.main.api.Player) this.player, component);
        Conduit.getEventManager().dispatchEvent(event);
        Component eventMessage = event.getMessage();
        if (eventMessage != null) playerList.broadcastMessage(eventMessage, chatType, UUID.randomUUID());
    }

    // TODO
//    @Inject(method = "handleCommand", at = @At("HEAD"))
//    private void handleCommand(String s, CallbackInfo ci) {
//        PlayerEvents.PlayerCommandEvent event = new PlayerEvents.PlayerCommandEvent((systems.conduit.main.api.Player) this.player, message);
//        Conduit.getEventManager().dispatchEvent(event);
//        return event.getMessage();
//    }

    @Redirect(method = "onDisconnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;)V"))
    private void playerLeaveMessage(PlayerList playerList, Component message, Component arg) {
        PlayerEvents.LeaveType leaveType = PlayerEvents.LeaveType.UNKNOWN;
        if (arg instanceof TranslatableComponent) {
            TranslatableComponent type = ((TranslatableComponent) arg);
            if (type.getKey().equalsIgnoreCase("multiplayer.disconnect.kicked")) {
                leaveType = PlayerEvents.LeaveType.KICKED;
            } else if (type.getKey().equalsIgnoreCase("multiplayer.disconnect.generic")) {
                leaveType = PlayerEvents.LeaveType.LEFT;
            }
        }
        PlayerEvents.PlayerLeaveEvent event = new PlayerEvents.PlayerLeaveEvent((systems.conduit.main.api.ServerPlayer) player, message, leaveType);
        Conduit.getEventManager().dispatchEvent(event);
        Component eventMessage = event.getMessage();
        if (eventMessage != null) playerList.broadcastMessage(event.getMessage(), ChatType.SYSTEM, UUID.randomUUID());
    }

    /**
     * @author ConduitMC
     */
    @Overwrite
    public void handleContainerClick(ServerboundContainerClickPacket packet) {
        player.resetLastActionTime();

        if (player.containerMenu.containerId != packet.getContainerId()) return;
        if (!player.containerMenu.isSynched(player)) return;

        if (player.isSpectator()) {
            NonNullList<ItemStack> items = NonNullList.of(ItemStack.EMPTY, player.containerMenu.slots.stream().map(Slot::getItem).collect(Collectors.toList()).toArray(new ItemStack[]{}));
            player.refreshContainer(player.containerMenu, items);
            return;
        }

        // If the player isn't a spectator, we need to submit an event to make sure that there's nothing stopping this from happening
        PlayerEvents.InventoryClickEvent event = new PlayerEvents.InventoryClickEvent((systems.conduit.main.api.ServerPlayer) player, player.containerMenu, packet.getClickType(), packet.getSlotNum());
        Conduit.getEventManager().dispatchEvent(event);

        System.out.println(event.isCanceled());
        if (!event.isCanceled()) {
            ItemStack clickResult = this.player.containerMenu.clicked(packet.getSlotNum(), packet.getButtonNum(), packet.getClickType(), player);

            if (ItemStack.matches(packet.getItem(), clickResult) && !event.isCanceled()) {
                player.connection.send(new ClientboundContainerAckPacket(packet.getContainerId(), packet.getUid(), true));
                player.containerMenu.broadcastChanges();
                player.connection.send(new ClientboundContainerSetSlotPacket(-1, -1, player.getInventory().getCarried()));
                return;
            }
        }

        expectedAcks.put(player.containerMenu.containerId, packet.getUid());
        player.connection.send(new ClientboundContainerAckPacket(packet.getContainerId(), packet.getUid(), false));
        player.containerMenu.setSynched(player, false);

        player.refreshContainer(player.containerMenu, NonNullList.of(ItemStack.EMPTY, player.containerMenu.slots.stream().map(Slot::getItem).collect(Collectors.toList()).toArray(new ItemStack[]{})));
    }
}
