package systems.conduit.versions.v1165.mixins.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.events.types.WorldEvents;

import java.util.Random;

/**
 * @author Innectic
 * @since 12/27/2020
 */
@Mixin(value = LeavesBlock.class, remap = false)
public class LeavesBlockMixin {

    @Inject(method = "randomTick", at = @At(value = "HEAD", target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"), cancellable = true)
    public void randomTick(BlockState blockState, ServerLevel level, BlockPos blockPos, Random random, CallbackInfo ci) {
        WorldEvents.LeafDecayEvent event = new WorldEvents.LeafDecayEvent(blockState, (systems.conduit.main.core.api.mixins.ServerLevel) level);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) ci.cancel();
    }
}
