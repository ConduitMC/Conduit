package systems.conduit.main.mixin.event.player;

import systems.conduit.main.Conduit;
import systems.conduit.main.events.EventType;

import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PlayerList.class, remap = false)
public abstract class JoinMixin {

    @Shadow public abstract void broadcastMessage(Component component);

    @Redirect(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;)V"))
    private void playerJoinMessage(PlayerList playerList, Component component) {
    }

    @Inject(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;sendLevelInfo(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/level/ServerLevel;)V"))
    private void onPlayerJoin(Connection connection, ServerPlayer player, CallbackInfo ci) {
        Component translateMsg = new TranslatableComponent("multiplayer.player.joined", player.getDisplayName());
        EventType.PlayerJoinEvent event = new EventType.PlayerJoinEvent(player, translateMsg);
        Conduit.eventManager.dispatchEvent(event);

        broadcastMessage(event.getMessage());
    }
}
