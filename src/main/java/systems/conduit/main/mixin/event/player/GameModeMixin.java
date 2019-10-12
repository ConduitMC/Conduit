package systems.conduit.main.mixin.event.player;

import systems.conduit.main.Conduit;
import systems.conduit.main.events.EventType;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.GameType;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerPlayerGameMode.class, remap = false)
public abstract class GameModeMixin {

    @Shadow public ServerPlayer player;
    @Shadow public ServerLevel level;

    @Shadow public abstract GameType getGameModeForPlayer();

    @Inject(method = "updateGameMode", at = @At("HEAD"))
    private void updateGameMode(GameType gameType, CallbackInfo ci) {
        // Don't call if the gamemode is the same
        if (getGameModeForPlayer() == gameType) return;
        // TODO: Event cancellations
        EventType.PlayerGameModeChangeEvent event = new EventType.PlayerGameModeChangeEvent(player, gameType);
        Conduit.eventManager.dispatchEvent(event);
    }

    @Inject(method = "destroyAndAck", at = @At("HEAD"))
    private void destroyAndAck(BlockPos blockPos, ServerboundPlayerActionPacket.Action action, CallbackInfo ci) {
        // TODO: Event cancellations
        EventType.BlockBreakEvent event = new EventType.BlockBreakEvent(player, level.getBlockState(blockPos));
        Conduit.eventManager.dispatchEvent(event);
    }
}
