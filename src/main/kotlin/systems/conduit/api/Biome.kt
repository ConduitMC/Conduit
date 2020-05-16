package systems.conduit.api

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.biome.Biome.*

/**
 * Exposed Biome API.
 *
 * @author Innectic
 * @since 11/28/2019
 */
interface Biome {
    fun isMutated(): Boolean
    fun getMobs(category: MobCategory?): List<SpawnerData?>?
    fun getPrecipitation(): Precipitation?
    fun isHumid(): Boolean
    fun getCreatureProbability(): Float
    fun getTemperature(pos: BlockPos?, bypassCache: Boolean): Float
    fun getTemperature(): Float
    fun shouldFreeze(reader: LevelReader?, pos: BlockPos?): Boolean
    fun shouldSnow(reader: LevelReader?, pos: BlockPos?): Boolean
    fun getDepth(): Float
    fun getDownfall(): Float
    fun getScale(): Float
    fun getWaterColor(): Int
    fun getWaterFogColor(): Int
    fun getBiomeCategory(): BiomeCategory?
    fun getParent(): String?
}