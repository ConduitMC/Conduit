package systems.conduit.main.api;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.GameProfiler;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelStorageSource;

import java.util.Map;
import java.util.concurrent.Executor;

public interface MinecraftServer {

    Executor getExecutor();
    ServerLevel getLevel(DimensionType dimensionType);
    LevelStorageSource getStorageSource();
    GameProfiler getProfiler();
    Map<DimensionType, ServerLevel> getLevels();

}
