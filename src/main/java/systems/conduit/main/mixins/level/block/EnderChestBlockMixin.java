package systems.conduit.main.mixins.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnderChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.mixins.ServerPlayer;
import systems.conduit.main.core.events.types.WorldEvents;

/**
 * @author Innectic
 * @since 12/27/2020
 */
@Mixin(value = EnderChestBlock.class, remap = false)
public class EnderChestBlockMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        WorldEvents.EnderChestOpenEvent event = new WorldEvents.EnderChestOpenEvent(blockPos, (systems.conduit.main.api.mixins.Level) level, blockState, (ServerPlayer) player, interactionHand);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            cir.setReturnValue(InteractionResult.PASS);
            cir.cancel();
        }
    }

    @Inject(method = "use", at = @At(value = "HEAD", target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;angerNearbyPiglins(Lnet/minecraft/world/entity/player/Player;Z)V"))
    public void useAnger(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        WorldEvents.PiglinAngerEvent event = new WorldEvents.PiglinAngerEvent((ServerPlayer) player, (systems.conduit.main.api.mixins.Level) level, new WorldEvents.PiglinAngerEvent.ChestSource(blockState));
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            cir.setReturnValue(InteractionResult.CONSUME);
            cir.cancel();
        }
    }
}
