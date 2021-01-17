package systems.conduit.main.mixins.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.LevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import systems.conduit.main.core.api.mixins.Biome;

import java.util.Optional;

/**
 * The exposed level API.
 *
 * @author Innectic
 * @since 11/28/2019
 */
@Mixin(value = Level.class, remap = false)
public abstract class LevelMixin implements systems.conduit.main.core.api.mixins.Level {

    @Shadow public abstract BiomeManager shadow$getBiomeManager();

    @Override
    public Biome getBiome(BlockPos pos) {
        return (Biome) (Object) this.shadow$getBiomeManager().getBiome(pos);
    }

    @Shadow public abstract LevelChunk getChunkAt(BlockPos pos);
    @Shadow public abstract LevelChunk getChunk(int x, int z);
    @Shadow public abstract boolean setBlock(BlockPos pos, BlockState state, int flag);
    @Shadow public abstract boolean removeBlock(BlockPos pos, boolean flag);
    @Shadow public abstract boolean setBlockAndUpdate(BlockPos pos, BlockState state);
    @Shadow public abstract BlockState getBlockState(BlockPos pos);
    @Shadow public abstract FluidState getFluidState(BlockPos pos);
    @Shadow public abstract boolean isDay();
    @Shadow public abstract void playSound(Player player, BlockPos pos, SoundEvent event, SoundSource source, float pitch, float volume);
    @Shadow public abstract BlockEntity shadow$getBlockEntity(BlockPos pos);
    @Shadow public abstract void setBlockEntity(BlockEntity entity);
    @Shadow public abstract void removeBlockEntity(BlockPos pos);
    @Shadow public abstract boolean isLoaded(BlockPos pos);
    @Shadow public abstract int getSeaLevel();
    @Shadow public abstract DimensionType dimensionType();
    @Shadow public abstract boolean hasSignal(BlockPos pos, Direction direction);
    @Shadow public abstract int getSignal(BlockPos pos, Direction direction);
    @Shadow public abstract boolean hasNeighborSignal(BlockPos pos);
    @Shadow public abstract int getBestNeighborSignal(BlockPos pos);
//    @Shadow public abstract long getSeed();
    @Shadow public abstract long getGameTime();
    @Shadow public abstract long getDayTime();
//    @Shadow public abstract void setDayTime(long time);
//    @Shadow public abstract BlockPos getSharedSpawnPos();
    @Shadow public abstract LevelData getLevelData();
    @Shadow public abstract GameRules getGameRules();
    @Shadow public abstract boolean isThundering();
    @Shadow public abstract boolean isRaining();
    @Shadow public abstract boolean isRainingAt(BlockPos pos);
    @Shadow public abstract boolean isHumidAt(BlockPos pos);
//    @Shadow public abstract int getHeight();
    @Shadow public abstract DifficultyInstance getCurrentDifficultyAt(BlockPos pos);
    @Shadow public abstract int getSkyDarken();
    @Shadow public abstract void setSkyFlashTime(int time);
    @Shadow public abstract WorldBorder getWorldBorder();

    public Optional<BlockEntity> getBlockEntity(BlockPos pos) {
        return Optional.ofNullable(shadow$getBlockEntity(pos));
    }

}
