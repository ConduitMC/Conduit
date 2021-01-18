package systems.conduit.main.mixins.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.api.mixins.ServerPlayer;
import systems.conduit.main.core.events.types.WorldEvents;

/**
 * @author Innectic
 * @since 12/27/2020
 */
@Mixin(value = NoteBlock.class,remap = false)
public class NoteBlockMixin {

    @Inject(method = "playNote", at = @At(value = "HEAD", target = "Lnet/minecraft/world/level/Level;blockEvent(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;II)V"), cancellable = true)
    private void playNote(Level level, BlockPos blockPos, CallbackInfo ci) {
        WorldEvents.NoteBlockPlayNoteEvent event = new WorldEvents.NoteBlockPlayNoteEvent((systems.conduit.main.core.api.mixins.Level) level, blockPos);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) ci.cancel();
    }

    @Inject(method = "use", at = @At(value = "HEAD", target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"), cancellable = true)
    public void use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        WorldEvents.NoteBlockTuneEvent event = new WorldEvents.NoteBlockTuneEvent(blockState, (systems.conduit.main.core.api.mixins.Level) level, blockPos, (ServerPlayer) player, interactionHand);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            cir.setReturnValue(InteractionResult.PASS);
            cir.cancel();
        }
    }
}
