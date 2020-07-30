package systems.conduit.main.mixins.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;
import java.util.Optional;

/**
 * The exposed Biome API.
 *
 * @author Innectic
 * @since 11/28/2019
 */
@Mixin(value = Biome.class, remap = false)
public abstract class BiomeMixin implements systems.conduit.main.api.Biome {

    @Shadow public abstract boolean isMutated();
    @Shadow public abstract List<Biome.SpawnerData> getMobs(MobCategory category);
    @Shadow public abstract Biome.Precipitation getPrecipitation();
    @Shadow public abstract boolean isHumid();
    @Shadow public abstract float getCreatureProbability();
    @Shadow protected abstract float getTemperatureNoCache(BlockPos pos);
    @Shadow public abstract boolean shouldFreeze(LevelReader reader, BlockPos pos);
    @Shadow public abstract boolean shouldSnow(LevelReader reader, BlockPos pos);
    @Shadow public abstract float getDepth();
    @Shadow public abstract float getDownfall();
    @Shadow public abstract float getScale();
    //@Shadow public abstract int getWaterColor();
    //@Shadow public abstract int getWaterFogColor();
    @Shadow public abstract Biome.BiomeCategory getBiomeCategory();

    @Shadow public abstract float shadow$getTemperature(BlockPos pos);
    @Shadow public abstract float getTemperature();

    public float getTemperature(BlockPos pos, boolean bypassCache) {
        if (bypassCache) return getTemperatureNoCache(pos);
        return shadow$getTemperature(pos);
    }

    @Shadow public abstract String shadow$getParent();

    public Optional<String> getParent() {
        return Optional.ofNullable(shadow$getParent());
    }
}
