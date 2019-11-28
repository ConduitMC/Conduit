package systems.conduit.main.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LevelReader;

import java.util.List;
import java.util.Optional;

/**
 * @author Innectic
 * @since 11/28/2019
 */
public interface Biome {

    boolean isMutated();
    List<net.minecraft.world.level.biome.Biome.SpawnerData> getMobs(MobCategory category);
    net.minecraft.world.level.biome.Biome.Precipitation getPrecipitation();
    boolean isHumid();
    float getCreatureProbability();
    float getTemperature(BlockPos pos, boolean bypassCache);
    boolean shouldFreeze(LevelReader reader, BlockPos pos);
    boolean shouldSnow(LevelReader reader, BlockPos pos);
    float getDepth();
    float getDownfall();
    float getScale();
    int getWaterColor();
    int getWaterFogColor();
    net.minecraft.world.level.biome.Biome.BiomeCategory getBiomeCategory();
    Optional<String> getParent();
}
