package systems.conduit.main.mixin.event.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.Player;
import systems.conduit.main.events.EventType;

@Mixin(value = ServerPlayer.class, remap = false)
public abstract class ServerPlayerMixin {

    @ModifyVariable(method = "setGameMode", at = @At("HEAD"))
    private GameType updateGameMode(GameType gameType) {
        // TODO: Event cancellations
        EventType.PlayerGameModeChangeEvent event = new EventType.PlayerGameModeChangeEvent((Player) this, gameType);
        Conduit.getEventManager().dispatchEvent(event);
        return event.getGamemode();
    }
}
