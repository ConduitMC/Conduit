package systems.conduit.versions.v116.common.mixins.player;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.api.mixins.Player;
import systems.conduit.main.core.events.types.WorldEvents;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = ServerPlayerGameMode.class, remap = false)
public abstract class ServerPlayerGameModeMixin {

    @Shadow @Final protected ServerPlayer player;
    @Shadow protected ServerLevel level;

    private List<BlockPos> cancelled = new ArrayList<>();

    @Inject(method = "destroyBlock", at = @At(value = "HEAD", target = "Lnet/minecraft/world/level/block/Block;playerWillDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)V"))
    public void destroyBlockInitial(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        WorldEvents.BlockBreakEvent event = new WorldEvents.BlockBreakEvent((Player) player, level.getBlockState(blockPos), blockPos);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) cancelled.add(blockPos);
    }

    @Inject(method = "destroyBlock", at = @At(value = "HEAD", target = "Lnet/minecraft/world/level/block/Block;playerWillDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)V"), cancellable = true)
    public void destroyBlockRemoveBlockCancellation(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        // If this location has been cancelled, then we're going to stop the normal destroy system.
        if (cancelled.contains(blockPos)) {
            // Since this location was cancelled, we're just going to cancel everything.
            cancelled.remove(blockPos);

            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
