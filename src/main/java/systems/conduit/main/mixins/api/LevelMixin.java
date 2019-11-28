package systems.conduit.main.mixins.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * @author Innectic
 * @since 11/28/2019
 */
@Mixin(value = Level.class, remap = false)
public abstract class LevelMixin implements systems.conduit.main.api.Level {

    @Shadow public abstract Biome getBiome(BlockPos pos);
}
