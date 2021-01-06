package systems.conduit.main.mixins.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.events.types.WorldEvents;

/**
 * @author Innectic
 * @since 12/27/2020
 */
@Mixin(value = JukeboxBlock.class, remap = false)
public class JukeboxMixin {

    @Inject(method = "setRecord", at = @At(value = "HEAD", target = "Lnet/minecraft/world/level/block/entity/JukeboxBlockEntity;setRecord(Lnet/minecraft/world/item/ItemStack;)V"), cancellable = true)
    public void setRecord(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, ItemStack record, CallbackInfo ci) {
        WorldEvents.JukeboxChangeRecordEvent event = new WorldEvents.JukeboxChangeRecordEvent(blockPos, blockState, record);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) ci.cancel();
    }

    @Inject(method = "dropRecording", at = @At(value = "HEAD", target = "Lnet/minecraft/world/level/LevelAccessor;levelEvent(ILnet/minecraft/core/BlockPos;I)V"), cancellable = true)
    public void dropRecording(Level level, BlockPos blockPos, CallbackInfo ci) {
        BlockEntity jukeboxEntity = level.getBlockEntity(blockPos);
        JukeboxBlockEntity jukebox = (JukeboxBlockEntity) jukeboxEntity;
        if (jukebox == null) return;

        ItemStack record = jukebox.getRecord();

        WorldEvents.JukeboxDropRecordEvent event = new WorldEvents.JukeboxDropRecordEvent(blockPos, jukebox, record, (systems.conduit.main.core.api.mixins.Level) level);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) ci.cancel();
    }
}
