package systems.conduit.core.mixins

import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.server.players.PlayerList
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.ModifyArg
import org.spongepowered.asm.mixin.injection.Redirect
import systems.conduit.api.Player
import systems.conduit.core.Conduit
import systems.conduit.core.events.types.PlayerEvents

@Mixin(value = [ServerGamePacketListenerImpl::class], remap = false)
class ServerGamePacketListenerMixin {

    @Shadow var player: ServerPlayer? = null

    @Redirect(method = ["handleChat"], at = At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Z)V"))
    private fun handleChat(playerList: PlayerList?, component: Component?, b: Boolean) {
        val event = PlayerEvents.PlayerChatEvent(player as Player, component)
        Conduit.eventManager.dispatchEvent(event)
        if (playerList != null && component != null) playerList.broadcastMessage(event.message, b)
    }

    @ModifyArg(method = ["handleChat"], at = At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;handleCommand(Ljava/lang/String;)V"))
    private fun handleCommand(message: String?): String? {
        var eventMessage = message
        if (eventMessage == null) eventMessage = ""
        val event = PlayerEvents.PlayerCommandEvent(player as Player, eventMessage)
        Conduit.eventManager.dispatchEvent(event)
        return event.message
    }

    @Redirect(method = ["onDisconnect"], at = At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;)V"))
    private fun playerLeaveMessage(playerList: PlayerList?, message: Component?, arg: Component?) {
        var leaveType: PlayerEvents.LeaveType = PlayerEvents.LeaveType.UNKNOWN
        if (arg is TranslatableComponent) {
            if (arg.key.equals("multiplayer.disconnect.kicked", ignoreCase = true)) {
                leaveType = PlayerEvents.LeaveType.KICKED
            } else if (arg.key.equals("multiplayer.disconnect.generic", ignoreCase = true)) {
                leaveType = PlayerEvents.LeaveType.LEFT
            }
        }
        val event = PlayerEvents.PlayerLeaveEvent(player as Player, message, leaveType)
        Conduit.eventManager.dispatchEvent(event)
        if (playerList != null && message != null) playerList.broadcastMessage(event.message)
    }
}
