package systems.conduit.core.mixins;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import systems.conduit.core.Conduit;
import systems.conduit.core.events.types.PlayerEvents;

@Mixin(value = ServerGamePacketListenerImpl.class, remap = false)
public class ServerGamePacketListenerMixin {

    @Shadow public ServerPlayer player;

    @Redirect(method = "handleChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Z)V"))
    private void handleChat(PlayerList playerList, Component component, boolean b) {
        PlayerEvents.PlayerChatEvent event = new PlayerEvents.PlayerChatEvent((systems.conduit.api.Player) this.player, component);
        Conduit.getEventManager().dispatchEvent(event);
        Component eventMessage = event.getMessage();
        if (eventMessage != null) playerList.broadcastMessage(eventMessage, b);
    }

    @ModifyArg(method = "handleChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;handleCommand(Ljava/lang/String;)V"))
    private String handleCommand(String message) {
        PlayerEvents.PlayerCommandEvent event = new PlayerEvents.PlayerCommandEvent((systems.conduit.api.Player) this.player, message);
        Conduit.getEventManager().dispatchEvent(event);
        return event.getMessage();
    }

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
        PlayerEvents.PlayerLeaveEvent event = new PlayerEvents.PlayerLeaveEvent((systems.conduit.api.ServerPlayer) player, message, leaveType);
        Conduit.getEventManager().dispatchEvent(event);
        Component eventMessage = event.getMessage();
        if (eventMessage != null) playerList.broadcastMessage(event.getMessage());
    }
}
