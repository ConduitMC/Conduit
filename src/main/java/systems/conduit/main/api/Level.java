package systems.conduit.main.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.LevelData;

import java.util.Optional;

/**
 * Exposed Level API.
 *
 * @author Innectic
 * @since 11/28/2019
 */
public interface Level {

    BiomeManager getBiomeManager();

    Biome getBiome(BlockPos pos);

    LevelChunk getChunkAt(BlockPos pos);
    LevelChunk getChunk(int x, int z);
    boolean setBlock(BlockPos pos, BlockState state, int flag);
    boolean removeBlock(BlockPos pos, boolean flag);
    boolean setBlockAndUpdate(BlockPos pos, BlockState state);
    BlockState getBlockState(BlockPos pos);
    FluidState getFluidState(BlockPos pos);
    boolean isDay();
    void playSound(Player player, BlockPos pos, SoundEvent event, SoundSource source, float pitch, float volume);
    Optional<BlockEntity> getBlockEntity(BlockPos pos);
    void setBlockEntity(BlockPos pos, BlockEntity entity);
    void removeBlockEntity(BlockPos pos);
    boolean isLoaded(BlockPos pos);
    int getSeaLevel();
    ResourceKey<DimensionType> dimensionTypeKey();
    DimensionType dimensionType();
    boolean hasSignal(BlockPos pos, Direction direction);
    int getSignal(BlockPos pos, Direction direction);
    boolean hasNeighborSignal(BlockPos pos);
    int getBestNeighborSignal(BlockPos pos);
    void setGameTime(long time);
    long getSeed();
    long getGameTime();
    long getDayTime();
    //  void setDayTime(long time);
    BlockPos getSharedSpawnPos();
    LevelData getLevelData();
    ChunkSource conduit$getChunkSource();
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
}
