package systems.conduit.versions.v1162.mixins.server;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.api.mixins.Player;
import systems.conduit.main.core.events.types.PlayerEvents;

import java.util.UUID;

/**
 * @author Innectic
 * @since 2/13/2021
 */
@Mixin(value = ServerGamePacketListenerImpl.class, remap = false)
public class ServerGamePacketListenerMixin {

    @Shadow public ServerPlayer player;

    @Redirect(method = "handleChat(Ljava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"))
    private void handleChat(PlayerList playerList, Component component, ChatType chatType, UUID uuid) {
        PlayerEvents.PlayerChatEvent event = new PlayerEvents.PlayerChatEvent((Player) this.player, component);
        Conduit.getEventManager().dispatchEvent(event);
        Component eventMessage = event.getMessage();
        if (eventMessage != null) playerList.broadcastMessage(eventMessage, chatType, UUID.randomUUID());
    }
}
