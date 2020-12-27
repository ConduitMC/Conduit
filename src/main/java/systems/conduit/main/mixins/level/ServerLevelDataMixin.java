package systems.conduit.main.mixins.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.mixins.ServerLevel;
import systems.conduit.main.api.mixins.ServerLevelData;
import systems.conduit.main.core.events.types.WorldEvents;

import java.util.Optional;

/**
 * @author Innectic
 * @since 11/25/2020
 */
@Mixin(value = {DerivedLevelData.class, PrimaryLevelData.class}, remap = false)
public abstract class ServerLevelDataMixin implements ServerLevelData {

    @Shadow public abstract String getLevelName();

    @Inject(method = "setThundering", at = @At("HEAD"))
    public void setThunderingMixin(boolean isThundering, CallbackInfo ci) {
        WorldEvents.ThunderChangeStateEvent event = new WorldEvents.ThunderChangeStateEvent(isThundering, Optional.empty());
        Conduit.getEventManager().dispatchEvent(event);

        isThundering = !event.isCanceled();
    }

    @Inject(method = "setRainTime", at = @At("HEAD"))
    public void setRainTimeMixin(int rainTime, CallbackInfo ci) {
        WorldEvents.RainChangeStateEvent event = new WorldEvents.RainChangeStateEvent(rainTime);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) rainTime = 0;
    }

    @Inject(method = "setThunderTime", at = @At("HEAD"))
    public void setThunderTimeMixin(int thunderTime, CallbackInfo ci) {
        WorldEvents.ThunderChangeStateEvent event = new WorldEvents.ThunderChangeStateEvent(thunderTime > 0, Optional.of(thunderTime));
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) thunderTime = 0;
    }

    @Inject(method = "setClearWeatherTime", at = @At("HEAD"))
    public void setClearWeatherTimeMixin(int clearTime, CallbackInfo ci) {
        WorldEvents.WeatherClearEvent event = new WorldEvents.WeatherClearEvent(clearTime);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) return;
    }

    @Inject(method = "setSpawn", at = @At("HEAD"))
    public void setSpawn(BlockPos blockPos, float v, CallbackInfo ci) {
        Optional<ServerLevel> level = Conduit.getLevelManager().getLevel(this.getLevelName());
        if (!level.isPresent()) {
            // Somehow couldn't find a level that clearly exists?
            Conduit.getLogger().error("INTERNAL ERROR: Could not find the level that had their spawn changed??");
            return;
        }
        // We have the level, so lets emit the event.
        WorldEvents.SpawnChangeEvent event = new WorldEvents.SpawnChangeEvent(blockPos, level.get());
        Conduit.getEventManager().dispatchEvent(event);
    }
}
