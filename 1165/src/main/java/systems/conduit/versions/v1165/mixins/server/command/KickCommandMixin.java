package systems.conduit.versions.v1165.mixins.server.command;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.commands.KickCommand;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.events.types.PlayerEvents;

import java.util.Collection;

/**
 * @author Innectic
 * @since 11/28/2020
 */
@Mixin(value = KickCommand.class, remap = false)
public class KickCommandMixin {

    /**
     * @author ConduitMC
     */
    @Inject(method = "kickPlayers", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/commands/KickCommand;kickPlayers(Lnet/minecraft/commands/CommandSourceStack;Ljava/util/Collection;Lnet/minecraft/network/chat/Component;)I"))
    private static void kickPlayers(CommandSourceStack sourceStack, Collection<ServerPlayer> players, Component component, CallbackInfoReturnable<Integer> callback) {
        players.forEach(p -> {
            // Before kicking the player, send out the event so we can check if it should be cancelled.
            PlayerEvents.KickEvent event = new PlayerEvents.KickEvent((systems.conduit.main.core.api.mixins.ServerPlayer) p, component);
            Conduit.getEventManager().dispatchEvent(event);

            // If the event was cancelled, don't proceed with kicking this player.
            if (event.isCanceled()) {
                callback.setReturnValue(0);
                callback.cancel();
                return;
            }

            p.connection.disconnect(component);
            Component successComponent = new TranslatableComponent("commands.kick.success", p.getDisplayName(), component);
            sourceStack.sendSuccess(successComponent, true);
        });
    }
}
