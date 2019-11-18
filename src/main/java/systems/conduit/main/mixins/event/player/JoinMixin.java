package systems.conduit.main.mixins.event.player;

import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import systems.conduit.main.Conduit;
import systems.conduit.main.events.types.PlayerEvents;

@Mixin(value = PlayerList.class, remap = false)
public abstract class JoinMixin {

    @Shadow public abstract void broadcastMessage(Component component);

    @Redirect(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;)V"))
    private void playerJoinMessage(PlayerList playerList, Component message, Connection connection, ServerPlayer player) {
        PlayerEvents.PlayerJoinEvent event = new PlayerEvents.PlayerJoinEvent((systems.conduit.main.api.ServerPlayer) player, message);
        Conduit.getEventManager().dispatchEvent(event);
        Component eventMessage = event.getMessage();
        if (eventMessage != null) this.broadcastMessage(event.getMessage());
    }
}
