package systems.conduit.main.mixins.api;

import net.minecraft.core.MappedRegistry;
import net.minecraft.world.level.dimension.LevelStem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import systems.conduit.main.api.WorldGenSettings;

import java.util.Optional;

/**
 * @author Innectic
 * @since 10/24/2020
 */
@Mixin(value = net.minecraft.world.level.levelgen.WorldGenSettings.class, remap = false)
public abstract class WorldGenSettingsMixin implements WorldGenSettings {

    @Shadow @Final private long seed;
    @Shadow @Final private boolean generateFeatures;
    @Shadow @Final private boolean generateBonusChest;
    @Shadow @Final private MappedRegistry<LevelStem> dimensions;
    @Shadow @Final private Optional<String> legacyCustomOptions;

    @Shadow public abstract boolean isDebug();
    @Shadow public abstract boolean isFlatWorld();

    @Override
    public long seed() {
        return this.seed;
    }

    @Override
    public boolean generateFeatures() {
        return this.generateFeatures;
    }

    @Override
    public boolean generateBonusChest() {
        return this.generateBonusChest;
    }

    @Override
    public Optional<String> legacyCustomOptions() {
        return this.legacyCustomOptions;
    }

    @Override
    public MappedRegistry<LevelStem> dimensions() {
        return this.dimensions;
    }
}
