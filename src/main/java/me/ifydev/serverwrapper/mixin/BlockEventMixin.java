package me.ifydev.serverwrapper.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author Innectic
 * @since 10/2/2019
 */
@Mixin(value = net.minecraft.server.level.ServerPlayerGameMode.class, remap = false)
public class BlockEventMixin {

    @Shadow private ServerLevel level;
    @Shadow private ServerPlayer player;

    /**
     * @author Innectic
     */
    @Overwrite
    public boolean destroyBlock(BlockPos pos) {
        BlockState block = this.level.getBlockState(pos);

        System.out.println("Block at " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + " has been broken!");
        return true;
    }
}
