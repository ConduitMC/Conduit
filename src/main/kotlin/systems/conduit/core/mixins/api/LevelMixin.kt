package systems.conduit.core.mixins.api

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundSource
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.LevelType
import net.minecraft.world.level.LightLayer
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.border.WorldBorder
import net.minecraft.world.level.chunk.ChunkSource
import net.minecraft.world.level.chunk.LevelChunk
import net.minecraft.world.level.dimension.Dimension
import net.minecraft.world.level.material.FluidState
import net.minecraft.world.level.storage.LevelData
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import systems.conduit.api.Level

/**
 * The exposed level API.
 *
 * @author Innectic
 * @since 11/28/2019
 */
@Mixin(value = [net.minecraft.world.level.Level::class], remap = false)
abstract class LevelMixin: Level {

    @Shadow
    abstract fun `shadow$getBiome`(pos: BlockPos?): Biome?

    @Shadow
    abstract override fun getTopBlockState(pos: BlockPos?): BlockState?

    @Shadow
    abstract override fun getChunkAt(pos: BlockPos?): LevelChunk?

    @Shadow
    abstract override fun getChunk(x: Int, z: Int): LevelChunk?

    @Shadow
    abstract override fun setBlock(pos: BlockPos?, state: BlockState?, flag: Int): Boolean

    @Shadow
    abstract override fun removeBlock(pos: BlockPos?, flag: Boolean): Boolean

    @Shadow
    abstract override fun destroyBlock(pos: BlockPos?, dropItems: Boolean): Boolean

    @Shadow
    abstract override fun setBlockAndUpdate(pos: BlockPos?, state: BlockState?): Boolean

    @Shadow
    abstract override fun getRawBrightness(pos: BlockPos?, mask: Int): Int

    @Shadow
    abstract override fun getBrightness(layer: LightLayer?, pos: BlockPos?): Int

    @Shadow
    abstract override fun getBlockState(pos: BlockPos?): BlockState?

    @Shadow
    abstract override fun getFluidState(pos: BlockPos?): FluidState?

    @Shadow
    abstract override fun isDay(): Boolean

    @Shadow
    abstract override fun playSound(player: Player?, pos: BlockPos?, event: SoundEvent?, source: SoundSource?, pitch: Float, volume: Float)

    @Shadow
    abstract fun `shadow$getBlockEntity`(pos: BlockPos?): BlockEntity?

    @Shadow
    abstract override fun setBlockEntity(pos: BlockPos?, entity: BlockEntity?)

    @Shadow
    abstract override fun removeBlockEntity(pos: BlockPos?)

    @Shadow
    abstract override fun isLoaded(pos: BlockPos?): Boolean

    @Shadow
    abstract override fun getSeaLevel(): Int

    @Shadow
    abstract override fun getGeneratorType(): LevelType?

    @Shadow
    abstract override fun hasSignal(pos: BlockPos?, direction: Direction?): Boolean

    @Shadow
    abstract override fun getSignal(pos: BlockPos?, direction: Direction?): Int

    @Shadow
    abstract override fun hasNeighborSignal(pos: BlockPos?): Boolean

    @Shadow
    abstract override fun getBestNeighborSignal(pos: BlockPos?): Int

    @Shadow
    abstract override fun setGameTime(time: Long)

    @Shadow
    abstract override fun getSeed(): Long

    @Shadow
    abstract override fun getGameTime(): Long

    @Shadow
    abstract override fun getDayTime(): Long

    @Shadow
    abstract override fun setDayTime(time: Long)

    @Shadow
    abstract override fun getSharedSpawnPos(): BlockPos?

    @Shadow
    abstract override fun setSpawnPos(pos: BlockPos?)

    @Shadow
    abstract override fun getChunkSource(): ChunkSource?

    @Shadow
    abstract override fun getLevelData(): LevelData?

    @Shadow
    abstract override fun getGameRules(): GameRules?

    @Shadow
    abstract override fun isThundering(): Boolean

    @Shadow
    abstract override fun isRaining(): Boolean

    @Shadow
    abstract override fun isRainingAt(pos: BlockPos?): Boolean

    @Shadow
    abstract override fun isHumidAt(pos: BlockPos?): Boolean

    @Shadow
    abstract override fun getHeight(): Int

    @Shadow
    abstract override fun getCurrentDifficultyAt(pos: BlockPos?): DifficultyInstance?

    @Shadow
    abstract override fun getSkyDarken(): Int

    @Shadow
    abstract override fun setSkyFlashTime(time: Int)

    @Shadow
    abstract override fun getWorldBorder(): WorldBorder?

    @Shadow
    abstract override fun getDimension(): Dimension?
    override fun getBiome(pos: BlockPos?): systems.conduit.api.Biome? {
        return `shadow$getBiome`(pos) as systems.conduit.api.Biome?
    }

    override fun getBlockEntity(pos: BlockPos?): BlockEntity? {
        return `shadow$getBlockEntity`(pos)
    }
}
