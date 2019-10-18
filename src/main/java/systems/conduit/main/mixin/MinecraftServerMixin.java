package systems.conduit.main.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.GameProfiler;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import systems.conduit.main.api.MinecraftServer;

import java.util.Map;
import java.util.concurrent.Executor;

@Mixin(value = net.minecraft.server.MinecraftServer.class, remap = false)
public abstract class MinecraftServerMixin implements MinecraftServer {

    @Shadow @Final private Executor executor;

    @Shadow public abstract ServerLevel getLevel(DimensionType dimensionType);
    @Shadow public abstract LevelStorageSource getStorageSource();

    @Shadow public abstract GameProfiler getProfiler();

    @Shadow @Final private Map<DimensionType, ServerLevel> levels;

    public Executor getExecutor() {
        return executor;
   }

   public Map<DimensionType, ServerLevel> getLevels() {
        return levels;
   }
}
