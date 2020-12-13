package systems.conduit.main.mixins.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import systems.conduit.main.api.mixins.Biome;

/**
 * The exposed Biome API.
 *
 * @author Innectic
 * @since 11/28/2019
 */
@Mixin(value = net.minecraft.world.level.biome.Biome.class, remap = false)
public abstract class BiomeMixin implements Biome {

    @Shadow public abstract net.minecraft.world.level.biome.Biome.Precipitation getPrecipitation();
    @Shadow public abstract boolean isHumid();
    @Shadow public abstract boolean shouldFreeze(LevelReader reader, BlockPos pos);
    @Shadow public abstract boolean shouldSnow(LevelReader reader, BlockPos pos);
    @Shadow public abstract float getDepth();
    @Shadow public abstract float getDownfall();
    @Shadow public abstract float getScale();
    @Shadow public abstract net.minecraft.world.level.biome.Biome.BiomeCategory getBiomeCategory();

    @Shadow public abstract float shadow$getTemperature(BlockPos pos);
}
