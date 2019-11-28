package systems.conduit.main.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelType;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.Dimension;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.LevelData;

import java.util.Optional;

/**
 * @author Innectic
 * @since 11/28/2019
 */
public interface Level {

    Biome getBiome(BlockPos pos);
    BlockState getTopBlockState(BlockPos pos);
    LevelChunk getChunkAt(BlockPos pos);
    LevelChunk getChunk(int x, int z);
    boolean setBlock(BlockPos pos, BlockState state, int flag);
    boolean removeBlock(BlockPos pos, boolean flag);
    boolean destroyBlock(BlockPos pos, boolean dropItems);
    boolean setBlockAndUpdate(BlockPos pos, BlockState state);
    int getRawBrightness(BlockPos pos, int mask);
    int getBrightness(LightLayer layer, BlockPos pos);
    BlockState getBlockState(BlockPos pos);
    FluidState getFluidState(BlockPos pos);
    boolean isDay();
    void playSound(Player player, BlockPos pos, SoundEvent event, SoundSource source, float pitch, float volume);
    Optional<BlockEntity> getBlockEntity(BlockPos pos);
    void setBlockEntity(BlockPos pos, BlockEntity entity);
    void removeBlockEntity(BlockPos pos);
    boolean isLoaded(BlockPos pos);
    int getSeaLevel();
    LevelType getGeneratorType();
    boolean hasSignal(BlockPos pos, Direction direction);
    int getSignal(BlockPos pos, Direction direction);
    boolean hasNeighborSignal(BlockPos pos);
    int getBestNeighborSignal(BlockPos pos);
    void setGameTime(long time);
    long getSeed();
    long getGameTime();
    long getDayTime();
    void setDayTime(long time);
    BlockPos getSharedSpawnPos();
    void setSpawnPos(BlockPos pos);
    ChunkSource getChunkSource();
    LevelData getLevelData();
    GameRules getGameRules();
    boolean isThundering();
    boolean isRaining();
    boolean isRainingAt(BlockPos pos);
    boolean isHumidAt(BlockPos pos);
    int getHeight();
    DifficultyInstance getCurrentDifficultyAt(BlockPos pos);
    int getSkyDarken();
    void setSkyFlashTime(int time);
    WorldBorder getWorldBorder();
    Dimension getDimension();
}
