package systems.conduit.main.mixin.event;

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
public abstract class ServerPlayerGameModeMixin {

    @Shadow public ServerPlayer player;
    @Shadow public ServerLevel level;

    @Inject(method = "updateGameMode", at = @At("HEAD"))
    private void updateGameMode(GameType gameType, CallbackInfo ci) {
        // TODO: Event cancellations
        Conduit.eventManager.dispatchEvent(new EventType.PlayerGameModeChangeEvent(player, gameType));
    }

    @Inject(method = "destroyAndAck", at = @At("HEAD"))
    private void destroyAndAck(BlockPos blockPos, ServerboundPlayerActionPacket.Action action, CallbackInfo ci) {
        // TODO: Event cancellations
        EventType.BlockBreakEvent event = new EventType.BlockBreakEvent(player, this.level.getBlockState(blockPos));
        Conduit.eventManager.dispatchEvent(event);
    }
}
