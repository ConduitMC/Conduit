package systems.conduit.main.api.mixins;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;

/**
 * Exposed Biome API.
 *
 * @author Innectic
 * @since 11/28/2019
 */
public interface Biome {

    net.minecraft.world.level.biome.Biome.Precipitation getPrecipitation();
    boolean isHumid();
    boolean shouldFreeze(LevelReader reader, BlockPos pos);
    boolean shouldSnow(LevelReader reader, BlockPos pos);
    float getDepth();
    float getDownfall();
    float getScale();
    int getWaterColor();
    int getWaterFogColor();
    net.minecraft.world.level.biome.Biome.BiomeCategory getBiomeCategory();
}
