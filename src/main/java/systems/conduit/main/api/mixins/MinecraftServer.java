package systems.conduit.main.api.mixins;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.bossevents.CustomBossEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import systems.conduit.main.mixins.server.MinecraftServerMixin;

import java.util.Map;
import java.util.concurrent.Executor;

/**
 * Base Conduit interface for use with mixins.
 * Implementation: {@link MinecraftServerMixin}
 *
 * @since API 0.1
 */
public interface MinecraftServer {

    Executor getExecutor();
    ServerLevel getLevel(ResourceKey<Level> dimensionType);
    Map<ResourceKey<Level>, ServerLevel> getLevels();
    Commands getCommands();
    boolean isStopped();
    boolean isRunning();
    CommandSourceStack createCommandSourceStack();
    void close();
    CustomBossEvents getCustomBossEvents();
    String getServerModName();
    Iterable<ServerLevel> getAllLevels();
    WorldData getWorldData();
    RegistryAccess.RegistryHolder getRegistryHolder();
    ChunkProgressListenerFactory getProgressListenerFactory();
    LevelStorageSource.LevelStorageAccess getStorageSource();
    PlayerList getPlayerList();
    ServerStatus getStatus();
}
