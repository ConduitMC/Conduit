package systems.conduit.core.managers

import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.progress.ChunkProgressListener
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.LevelSettings
import net.minecraft.world.level.chunk.ChunkStatus
import net.minecraft.world.level.dimension.DimensionType
import systems.conduit.api.MinecraftServer
import systems.conduit.core.Conduit
import java.util.*

// TODO: This needs to be moved to the API
class LevelManager {
    /**
     * Attempt to find a level by a given name.
     *
     * @since API 0.1
     * @param name the name of the level
     * @return a level with the name, if found.
     */
    fun getLevel(name: String?): ServerLevel? {
        return Conduit.server.getLevel(DimensionType.getByName(ResourceLocation.of(name, '/')))
    }

    /**
     * Create a new level with given information.
     *
     * @since API 0.1
     *
     * @param name the name of the level
     * @param dimension dimension metadata
     * @return the created level, if it could be created. Empty otherwise.
     */
    fun createLevel(name: String?, dimension: DimensionType?): ServerLevel? {
        if (Conduit.server.getStorageSource() == null) return null
        val storage = Conduit.server.getStorageSource()!!.selectLevel(name, Conduit.server as net.minecraft.server.MinecraftServer?)
        val data = storage.prepareLevel() ?: return null
        data.levelName = name
        val settings = LevelSettings(data)
        val level = ServerLevel(Conduit.server as net.minecraft.server.MinecraftServer?, Conduit.server.getExecutor(), storage, data, dimension, Conduit.server.getProfiler(), object: ChunkProgressListener {
            override fun updateSpawnPos(chunkPos: ChunkPos) {}
            override fun onStatusChange(chunkPos: ChunkPos, chunkStatus: ChunkStatus?) {}
            override fun stop() {}
        })
        if (!data.isInitialized) {
            level.setInitialSpawn(settings)
            data.isInitialized = true
        }
        Conduit.server.levels[dimension] = level
        return level
    }
}