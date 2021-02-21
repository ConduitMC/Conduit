package systems.conduit.versions.v116.mixins.server;

import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
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

    @Redirect(method = "handleChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;handleChat(Lnet/minecraft/network/protocol/game/ServerboundChatPacket;)V"))
    private void handleChat(ServerGamePacketListenerImpl listener, ServerboundChatPacket packet) {
        PlayerEvents.PlayerChatEvent event = new PlayerEvents.PlayerChatEvent((Player) this.player, new TextComponent(packet.getMessage()));
        Conduit.getEventManager().dispatchEvent(event);
        Component eventMessage = event.getMessage();
        if (eventMessage != null) Conduit.getServer().ifPresent(s -> s.getPlayerList().broadcastMessage(eventMessage, ChatType.CHAT, UUID.randomUUID()));
    }
}
