package systems.conduit.main.api.mixins;

import net.minecraft.core.MappedRegistry;
import net.minecraft.world.level.dimension.LevelStem;

import java.util.Optional;

/**
 * @author Innectic
 * @since 10/24/2020
 */
public interface WorldGenSettings {

    long seed();
    boolean generateFeatures();
    boolean generateBonusChest();

    Optional<String> legacyCustomOptions();
    MappedRegistry<LevelStem> dimensions();

    boolean isDebug();
    boolean isFlatWorld();
}
