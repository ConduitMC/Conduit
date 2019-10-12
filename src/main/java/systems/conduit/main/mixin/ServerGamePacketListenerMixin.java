package systems.conduit.main.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import systems.conduit.main.Conduit;
import systems.conduit.main.events.EventType;

@Mixin(value = ServerGamePacketListenerImpl.class, remap = false)
public class ServerGamePacketListenerMixin {

    @Shadow public ServerPlayer player;

    @ModifyArg(method = "handleChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Z)V"))
    private Component handleChat(Component messageComp) {
        TranslatableComponent translatedMessage = (TranslatableComponent) messageComp;
        String message = (String) translatedMessage.getArgs()[1];

        // TODO use component here?
        EventType.PlayerChatEvent event = new EventType.PlayerChatEvent(this.player, message);

        Conduit.eventManager.dispatchEvent(event);
        return new TranslatableComponent("chat.type.text", this.player.getDisplayName(), event.getMessage());
    }

    @ModifyArg(method = "handleChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;handleCommand(Ljava/lang/String;)V"))
    private String handleCommand(String message) {
        EventType.PlayerCommandEvent event = new EventType.PlayerCommandEvent(this.player, message);
        Conduit.eventManager.dispatchEvent(event);
        return event.getMessage();
    }
}
