package systems.conduit.core.mixins.apiimport

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
    @get:Shadow
    abstract override val isMutated: Boolean

    @Shadow
    abstract fun getMobs(category: MobCategory?): List<SpawnerData?>?

    @get:Shadow
    abstract override val precipitation: net.minecraft.world.level.biome.Biome.Precipitation?

    @get:Shadow
    abstract override val isHumid: Boolean

    @get:Shadow
    abstract override val creatureProbability: Float

    @Shadow
    protected abstract fun getTemperatureNoCache(pos: BlockPos?): Float

    @Shadow
    abstract fun shouldFreeze(reader: LevelReader?, pos: BlockPos?): Boolean

    @Shadow
    abstract fun shouldSnow(reader: LevelReader?, pos: BlockPos?): Boolean

    @get:Shadow
    abstract override val depth: Float

    @get:Shadow
    abstract override val downfall: Float

    @get:Shadow
    abstract override val scale: Float

    @get:Shadow
    abstract override val waterColor: Int

    @get:Shadow
    abstract override val waterFogColor: Int

    @get:Shadow
    abstract override val biomeCategory: net.minecraft.world.level.biome.Biome.BiomeCategory?

    @Shadow
    abstract fun `shadow$getTemperature`(pos: BlockPos?): Float

    @get:Shadow
    abstract override val temperature: Float
    override fun getTemperature(pos: BlockPos?, bypassCache: Boolean): Float {
        return if (bypassCache) getTemperatureNoCache(pos) else `shadow$getTemperature`(pos)
    }

    @Shadow
    abstract fun `shadow$getParent`(): String?
    override val parent: Optional<String>
        get() = Optional.ofNullable(`shadow$getParent`())
}