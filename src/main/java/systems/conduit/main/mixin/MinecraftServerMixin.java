package systems.conduit.main.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.GameProfiler;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import systems.conduit.main.api.ConduitServer;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author Innectic
 * @since 10/16/2019
 */
@Mixin(value = MinecraftServer.class, remap = false)
public abstract class MinecraftServerMixin implements ConduitServer {

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
