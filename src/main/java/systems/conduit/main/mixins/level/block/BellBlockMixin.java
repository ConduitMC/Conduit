package systems.conduit.main.mixins.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.mixins.ServerPlayer;
import systems.conduit.main.core.events.types.WorldEvents;

/**
 * @author Innectic
 * @since 12/27/2020
 */
@Mixin(value = BellBlock.class, remap = false)
public class BellBlockMixin {

    @Inject(method = "onProjectileHit", at = @At("HEAD"), cancellable = true)
    public void onProjectileHit(Level level, BlockState blockState, BlockHitResult blockHitResult, Projectile projectile, CallbackInfo ci) {
        WorldEvents.ProjectileRingBellEvent event = new WorldEvents.ProjectileRingBellEvent((systems.conduit.main.api.mixins.Level) level, blockState, projectile);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) ci.cancel();
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        WorldEvents.PlayerRingBellEvent event = new WorldEvents.PlayerRingBellEvent((systems.conduit.main.api.mixins.Level) level, blockState, (ServerPlayer) player, interactionHand, blockPos);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            cir.setReturnValue(InteractionResult.PASS);
            cir.cancel();
        }
    }
}
