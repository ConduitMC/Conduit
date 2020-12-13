package systems.conduit.main.mixins.level;

import net.minecraft.core.MappedRegistry;
import net.minecraft.world.level.dimension.LevelStem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import systems.conduit.main.api.mixins.WorldGenSettings;

import java.util.Optional;

/**
 * @author Innectic
 * @since 10/24/2020
 */
@Mixin(value = net.minecraft.world.level.levelgen.WorldGenSettings.class, remap = false)
public abstract class WorldGenSettingsMixin implements WorldGenSettings {

    @Accessor public abstract long getSeed();
    @Shadow @Final private boolean generateFeatures;
    @Shadow @Final private boolean generateBonusChest;
    @Shadow @Final private MappedRegistry<LevelStem> dimensions;
    @Shadow @Final private Optional<String> legacyCustomOptions;

    @Shadow public abstract boolean isDebug();
    @Shadow public abstract boolean isFlatWorld();

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
