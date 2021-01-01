package systems.conduit.main.mixins.player;

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
import systems.conduit.main.core.events.types.WorldEvents;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = ServerPlayerGameMode.class, remap = false)
public abstract class ServerPlayerGameModeMixin {

    @Shadow @Final protected ServerPlayer player;
    @Shadow protected ServerLevel level;

    private List<BlockPos> cancelled = new ArrayList<>();

    @Inject(method = "destroyBlock", at = @At("HEAD"))
    private void destroyBlockInitial(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        // TODO: Event cancellations
        WorldEvents.BlockBreakEvent event = new WorldEvents.BlockBreakEvent((systems.conduit.main.api.mixins.ServerPlayer) player, level.getBlockState(blockPos));
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            cancelled.add(blockPos);
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "destroyBlock", at = @At(value = "HEAD", target = "Lnet/minecraft/world/level/block/Block;playerDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/item/ItemStack;)V"), cancellable = true)
    public void destroyBlockCancellation(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (cancelled.contains(blockPos)) {
            cancelled.remove(blockPos);
            cir.setReturnValue(false);
            cir.cancel();
        }
    }
}
