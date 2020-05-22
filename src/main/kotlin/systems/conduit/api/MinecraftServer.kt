package systems.conduit.api

import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.server.bossevents.CustomBossEvents
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.profiling.GameProfiler
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.storage.LevelStorageSource
import java.util.concurrent.Executor


/**
 * Base Conduit interface for use with mixins.
 * Implementation: [systems.conduit.core.mixins.MinecraftServerMixin]
 *
 * @since API 0.1
 */
interface MinecraftServer {
    fun getExecutor(): Executor?
    fun getLevel(dimensionType: DimensionType?): ServerLevel?
    fun getStorageSource(): LevelStorageSource?
    fun getProfiler(): GameProfiler?
    fun getLevels(): MutableMap<DimensionType?, ServerLevel?>
    fun getCommands(): Commands
    fun isStopped(): Boolean
    fun isRunning(): Boolean
    fun createCommandSourceStack(): CommandSourceStack?
    fun close()
    fun getCustomBossEvents(): CustomBossEvents
    fun getServerModName(): String?
}
