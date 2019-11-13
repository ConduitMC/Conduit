package systems.conduit.main.mixins.event.player;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.events.EventType;

@Mixin(value = ServerPlayerGameMode.class, remap = false)
public abstract class ServerPlayerGameModeMixin {

    @Shadow public ServerPlayer player;
    @Shadow public ServerLevel level;

    @Inject(method = "destroyBlock", at = @At("HEAD"))
    private void destroyAndAck(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        // TODO: Event cancellations
        EventType.BlockBreakEvent event = new EventType.BlockBreakEvent((systems.conduit.main.api.ServerPlayer) player, level.getBlockState(blockPos));
        Conduit.getEventManager().dispatchEvent(event);
        if (event.isCanceled()) {
            return;
        }
    }
}
