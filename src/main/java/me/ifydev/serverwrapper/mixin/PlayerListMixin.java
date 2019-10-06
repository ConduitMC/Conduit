package me.ifydev.serverwrapper.mixin;

import me.ifydev.serverwrapper.ServerWrapper;
import me.ifydev.serverwrapper.events.EventType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = PlayerList.class, remap = false)
public abstract class PlayerListMixin {

    @Shadow public abstract void broadcastMessage(Component component);

    @Redirect(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;)V"))
    private void playerJoinMessage(PlayerList playerList, Component component) {
    }

    @Redirect(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addNewPlayer(Lnet/minecraft/server/level/ServerPlayer;)V"))
    private void onPlayerJoin(ServerLevel serverLevel, ServerPlayer player) {
        Component translateMsg = new TranslatableComponent("multiplayer.player.joined", player.getDisplayName());
        EventType.PlayerJoinEvent event = new EventType.PlayerJoinEvent(player, translateMsg);
        ServerWrapper.eventManager.dispatchEvent(event);

        broadcastMessage(event.getMessage());
        serverLevel.addNewPlayer(player);
    }
}
