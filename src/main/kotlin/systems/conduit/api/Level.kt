package systems.conduit.api

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.LevelType
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.border.WorldBorder
import net.minecraft.world.level.chunk.ChunkSource
import net.minecraft.world.level.chunk.LevelChunk
import net.minecraft.world.level.dimension.Dimension
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.storage.LevelData
import java.util.*

/**
 * Exposed Level API.
 *
 * @author Innectic
 * @since 11/28/2019
 */
interface Level {
    fun getBiome(pos: BlockPos?): Biome?
    fun getTopBlockState(pos: BlockPos?): BlockState?
    fun getChunkAt(pos: BlockPos?): LevelChunk?
    fun getChunk(x: Int, z: Int): LevelChunk?
    fun setBlock(pos: BlockPos?, state: BlockState?, flag: Int): Boolean
    fun removeBlock(pos: BlockPos?, flag: Boolean): Boolean
    fun destroyBlock(pos: BlockPos?, dropItems: Boolean): Boolean
    fun setBlockAndUpdate(pos: BlockPos?, state: BlockState?): Boolean
    fun getRawBrightness(pos: BlockPos?, mask: Int): Int
    fun getBrightness(layer: LightLayer?, pos: BlockPos?): Int
    fun getBlockState(pos: BlockPos?): BlockState?
    fun getFluidState(pos: BlockPos?): FluidState?
    fun isDay(): Boolean
    fun playSound(player: Player?, pos: BlockPos?, event: SoundEvent?, source: SoundSource?, pitch: Float, volume: Float)
    fun getBlockEntity(pos: BlockPos?): BlockEntity?
    fun setBlockEntity(pos: BlockPos?, entity: BlockEntity?)
    fun removeBlockEntity(pos: BlockPos?)
    fun isLoaded(pos: BlockPos?): Boolean
    fun getSeaLevel(): Int
    fun getGeneratorType(): LevelType?
    fun hasSignal(pos: BlockPos?, direction: Direction?): Boolean
    fun getSignal(pos: BlockPos?, direction: Direction?): Int
    fun hasNeighborSignal(pos: BlockPos?): Boolean
    fun getBestNeighborSignal(pos: BlockPos?): Int
    fun setGameTime(time: Long)
    fun getSeed(): Long
    fun getGameTime(): Long
    fun getDayTime(): Long
    fun setDayTime(time: Long)
    fun getSharedSpawnPos(): BlockPos?
    fun setSpawnPos(pos: BlockPos?)
    fun getChunkSource(): ChunkSource?
    fun getLevelData(): LevelData?
    fun getGameRules(): GameRules?
    fun isThundering(): Boolean
    fun isRaining(): Boolean
    fun isRainingAt(pos: BlockPos?): Boolean
    fun isHumidAt(pos: BlockPos?): Boolean
    fun getHeight(): Int
    fun getCurrentDifficultyAt(pos: BlockPos?): DifficultyInstance?
    fun getSkyDarken(): Int
    fun setSkyFlashTime(time: Int)
    fun getWorldBorder(): WorldBorder?
    fun getDimension(): Dimension?
}