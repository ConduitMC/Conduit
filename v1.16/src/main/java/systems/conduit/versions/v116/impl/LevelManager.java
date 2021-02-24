package systems.conduit.versions.v116.impl;

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
import systems.conduit.main.core.api.factories.LevelDataFactory;
import systems.conduit.main.core.api.mixins.MinecraftServer;
import systems.conduit.main.core.api.mixins.ServerLevel;

import java.util.Optional;

/**
 * @author Innectic
 * @since 2/13/2021
 */
public class LevelManager {

//    @Override
    public Optional<ServerLevel> getLevel(String name) {
        return Conduit.getServer().map(server -> {
            for (net.minecraft.server.level.ServerLevel level : server.getAllLevels()) {
                ServerLevel conduitLevel = (ServerLevel) level;
                if (conduitLevel.getServerLevelData().getLevelName().equals(name)) return conduitLevel;
            }
            return null;
        });
    }

//    @Override
    public Optional<net.minecraft.server.level.ServerLevel> createLevel(LevelDataFactory levelData) {
        Optional<MinecraftServer> server = Conduit.getServer();
        if (!server.isPresent()) return Optional.empty();

        ResourceKey<Level> resourceKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(levelData.getLevelName()));

        systems.conduit.main.core.api.mixins.WorldGenSettings worldGenSettings = (systems.conduit.main.core.api.mixins.WorldGenSettings) server.get().getWorldData().worldGenSettings();
        boolean isDebugMode = worldGenSettings.isDebug();
        long seed = BiomeManager.obfuscateSeed(worldGenSettings.seed());

        PrimaryLevelData newLevelData = generateNewLevelData(levelData, worldGenSettings.dimensions());
        ChunkGenerator chunkGenerator = generateChunkGenerator(levelData, seed);
        Optional<? extends Registry<DimensionType>> dimensionTypeRegistry = server.get().getRegistryHolder().registry(Registry.DIMENSION_TYPE_REGISTRY);
        if (!dimensionTypeRegistry.isPresent()) return Optional.empty();

        DimensionType dimensionType = dimensionTypeRegistry.get().get(levelData.getDimensionType());
        if (dimensionType == null) return Optional.empty();

        net.minecraft.server.level.ServerLevel newLevel = new net.minecraft.server.level.ServerLevel(
                (net.minecraft.server.MinecraftServer) server.get(), server.get().getExecutor(), server.get().getStorageSource(),
                newLevelData, resourceKey, levelData.getDimensionType(), dimensionType, server.get().getProgressListenerFactory().create(11), chunkGenerator,
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

    private ChunkGenerator generateChunkGenerator(LevelDataFactory levelData, long seed) {
        if (levelData.getDimensionType().equals(DimensionType.OVERWORLD_LOCATION)) {
            OverworldBiomeSource biomeSource = new OverworldBiomeSource(seed, false, levelData.isLargeBiomes());
            return new NoiseBasedChunkGenerator(biomeSource, seed, NoiseGeneratorSettings.Preset.OVERWORLD.settings());
        }

        // TODO: Nether and End biome sources

        return null;
    }
}
