package systems.conduit.main.mixin.event.player;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.Player;
import systems.conduit.main.events.EventType;

@Mixin(value = ServerPlayerGameMode.class, remap = false)
public abstract class ServerPlayerGameModeMixin {

    @Shadow public ServerPlayer player;
    @Shadow public ServerLevel level;

    @Inject(method = "destroyAndAck", at = @At("HEAD"))
    private void destroyAndAck(BlockPos blockPos, ServerboundPlayerActionPacket.Action action, CallbackInfo ci) {
        // TODO: Event cancellations
        EventType.BlockBreakEvent event = new EventType.BlockBreakEvent((Player) player, level.getBlockState(blockPos));
        Conduit.getEventManager().dispatchEvent(event);
    }
}
