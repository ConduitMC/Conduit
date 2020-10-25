package systems.conduit.main.api.managers;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.OverworldBiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.PrimaryLevelData;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.MinecraftServer;
import systems.conduit.main.api.ServerLevel;
import systems.conduit.main.api.factories.LevelDataFactory;

import java.util.Optional;

public class LevelManager {

    /**
     * Attempt to find a level by a given name.
     *
     * @since API 0.1
     * @param name the name of the level
     * @return a level with the name, if found.
     */
    public Optional<ServerLevel> getLevel(String name) {
        return Conduit.getServer().map(server -> {
            for (net.minecraft.server.level.ServerLevel level : server.getAllLevels()) {
                ServerLevel conduitLevel = (ServerLevel) level;
                if (conduitLevel.getServerLevelData().getLevelName().equals(name)) return conduitLevel;
            }
            return null;
        });
    }

    /**
     * Create a new level with given information.
     *
     * @since API 0.1
     *
     * @param levelData data used to create the new dimension
     * @return the created level, if it could be created. Empty otherwise.
     */
    public Optional<net.minecraft.server.level.ServerLevel> createLevel(LevelDataFactory levelData) {
        Optional<MinecraftServer> server = Conduit.getServer();
        if (!server.isPresent()) return Optional.empty();

        ResourceKey<Level> resourceKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(levelData.getLevelName()));

        systems.conduit.main.api.WorldGenSettings worldGenSettings = (systems.conduit.main.api.WorldGenSettings) server.get().getWorldData().worldGenSettings();
        boolean isDebugMode = worldGenSettings.isDebug();
        long seed = BiomeManager.obfuscateSeed(worldGenSettings.seed());

        PrimaryLevelData newLevelData = generateNewLevelData(levelData, worldGenSettings.dimensions());
        ChunkGenerator chunkGenerator = generateChunkGenerator(levelData, seed, server.get());
        DimensionType dimensionType = server.get().getRegistryHolder().dimensionTypes().getOrThrow(levelData.getDimensionType());

        net.minecraft.server.level.ServerLevel newLevel = new net.minecraft.server.level.ServerLevel(
                (net.minecraft.server.MinecraftServer) server.get(), server.get().getExecutor(), server.get().getStorageSource(),
                newLevelData, resourceKey, dimensionType, server.get().getProgressListenerFactory().create(11), chunkGenerator,
                isDebugMode, seed, LevelDataFactory.DEFAULT_CUSTOM_SPAWNERS, true);
        server.get().getLevels().put(resourceKey, newLevel);

        // TODO: World border

        return Optional.of(newLevel);
    }

    private PrimaryLevelData generateNewLevelData(LevelDataFactory levelData, MappedRegistry<LevelStem> dimensions) {
        LevelSettings settings = new LevelSettings(levelData.getLevelName(), levelData.getGameType(), levelData.isHardcore(),
                levelData.getDifficulty(), levelData.isAllowCommands(), levelData.getGameRules(), levelData.getDataPackConfig());
        WorldGenSettings worldGenSettings = new WorldGenSettings(levelData.getSeed(), levelData.isGenerateFeatures(),
                levelData.isGenerateBonusChest(), dimensions);

        return new PrimaryLevelData(settings, worldGenSettings, Lifecycle.stable());
    }

    private ChunkGenerator generateChunkGenerator(LevelDataFactory levelData, long seed, MinecraftServer server) {
        if (levelData.getDimensionType().equals(DimensionType.OVERWORLD_LOCATION)) {
            OverworldBiomeSource biomeSource = new OverworldBiomeSource(seed, false, levelData.isLargeBiomes(), server.getRegistryHolder().registryOrThrow(Registry.BIOME_REGISTRY));
            return new NoiseBasedChunkGenerator(biomeSource, seed, () -> server.getRegistryHolder().registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY).getOrThrow(NoiseGeneratorSettings.OVERWORLD));
        }

        // TODO: Nether and End biome sources

        return null;
    }
}
