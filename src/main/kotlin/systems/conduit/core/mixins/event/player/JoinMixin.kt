package systems.conduit.core.mixins.event.player

import net.minecraft.network.Connection
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.players.PlayerList
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect
import systems.conduit.core.Conduit
import systems.conduit.core.events.types.PlayerEvents

@Mixin(value = [PlayerList::class], remap = false)
abstract class JoinMixin {

    @Shadow abstract fun broadcastMessage(component: Component)

    @Redirect(method = ["placeNewPlayer"], at = At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;)V"))
    private fun playerJoinMessage(playerList: PlayerList, message: Component, connection: Connection, player: ServerPlayer) {
        val event = PlayerEvents.PlayerJoinEvent(player as systems.conduit.api.ServerPlayer, message)
        Conduit.eventManager.dispatchEvent(event)
        if (event.message != null) broadcastMessage(event.message!!)
    }
}