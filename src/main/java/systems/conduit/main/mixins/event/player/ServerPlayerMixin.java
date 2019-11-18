package systems.conduit.main.mixins.event.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.Player;
import systems.conduit.main.events.types.PlayerEvents;

@Mixin(value = ServerPlayer.class, remap = false)
public abstract class ServerPlayerMixin {

    @ModifyVariable(method = "setGameMode", at = @At("HEAD"))
    private GameType updateGameMode(GameType gameType) {
        // TODO: Allow this event to be cancelled
        //
        // We'll need to find a different way to hook into gamemode changes.

        PlayerEvents.PlayerGameModeChangeEvent event = new PlayerEvents.PlayerGameModeChangeEvent((Player) this, gameType);
        Conduit.getEventManager().dispatchEvent(event);
        return event.getGamemode();
    }
}
