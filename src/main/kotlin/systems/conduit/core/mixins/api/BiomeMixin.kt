package systems.conduit.core.mixins.api

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.biome.Biome.*
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import systems.conduit.api.Biome
import java.util.*


/**
 * The exposed Biome API.
 *
 * @author Innectic
 * @since 11/28/2019
 */
@Mixin(value = [net.minecraft.world.level.biome.Biome::class], remap = false)
abstract class BiomeMixin: Biome {
    @Shadow
    abstract override fun isMutated(): Boolean

    @Shadow
    abstract override fun getMobs(category: MobCategory?): List<SpawnerData?>?

    @Shadow
    abstract override fun getPrecipitation(): Precipitation?

    @Shadow
    abstract override fun isHumid(): Boolean

    @Shadow
    abstract override fun getCreatureProbability(): Float

    @Shadow
    protected abstract fun getTemperatureNoCache(pos: BlockPos?): Float

    @Shadow
    abstract override fun shouldFreeze(reader: LevelReader?, pos: BlockPos?): Boolean

    @Shadow
    abstract override fun shouldSnow(reader: LevelReader?, pos: BlockPos?): Boolean

    @Shadow
    abstract override fun getDepth(): Float

    @Shadow
    abstract override fun getDownfall(): Float

    @Shadow
    abstract override fun getScale(): Float

    @Shadow
    abstract override fun getWaterColor(): Int

    @Shadow
    abstract override fun getWaterFogColor(): Int

    @Shadow
    abstract override fun getBiomeCategory(): BiomeCategory?

    @Shadow
    abstract fun `shadow$getTemperature`(pos: BlockPos?): Float

    @Shadow
    abstract override fun getTemperature(): Float
    override fun getTemperature(pos: BlockPos?, bypassCache: Boolean): Float {
        return if (bypassCache) getTemperatureNoCache(pos) else `shadow$getTemperature`(pos)
    }

    @Shadow
    abstract fun `shadow$getParent`(): String?

    override fun getParent(): String? {
        return `shadow$getParent`()
    }
}
