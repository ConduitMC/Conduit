package systems.conduit.main.api;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.bossevents.CustomBossEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Base Conduit interface for use with mixins.
 * Implementation: {@link systems.conduit.main.mixins.MinecraftServerMixin}
 *
 * @since API 0.1
 */
public interface MinecraftServer {

    Executor getExecutor();
    ServerLevel getLevel(ResourceKey<Level> dimensionType);
    Map<DimensionType, ServerLevel> getLevels();
    Commands getCommands();
    boolean isStopped();
    boolean isRunning();
    CommandSourceStack createCommandSourceStack();
    void close();
    CustomBossEvents getCustomBossEvents();
    String getServerModName();
    Iterable<ServerLevel> getAllLevels();
}
