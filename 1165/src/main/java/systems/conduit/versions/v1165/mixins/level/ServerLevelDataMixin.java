package systems.conduit.versions.v1165.mixins.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.api.mixins.ServerLevelData;
import systems.conduit.main.core.events.types.WorldEvents;

import java.util.Optional;

/**
 * @author Innectic
 * @since 11/25/2020
 */
@Mixin(value = {DerivedLevelData.class, PrimaryLevelData.class}, remap = false)
public abstract class ServerLevelDataMixin implements ServerLevelData {

    @Inject(method = "setThundering", at = @At("HEAD"), cancellable = true)
    public void setThunderingMixin(boolean isThundering, CallbackInfo ci) {
        WorldEvents.ThunderChangeStateEvent event = new WorldEvents.ThunderChangeStateEvent(isThundering, Optional.empty());
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) ci.cancel();
    }

    @Inject(method = "setRainTime", at = @At("HEAD"), cancellable = true)
    public void setRainTimeMixin(int rainTime, CallbackInfo ci) {
        WorldEvents.RainChangeStateEvent event = new WorldEvents.RainChangeStateEvent(rainTime);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) ci.cancel();
    }

    @Inject(method = "setThunderTime", at = @At("HEAD"), cancellable = true)
    public void setThunderTimeMixin(int thunderTime, CallbackInfo ci) {
        WorldEvents.ThunderChangeStateEvent event = new WorldEvents.ThunderChangeStateEvent(thunderTime > 0, Optional.of(thunderTime));
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) ci.cancel();
    }

    @Inject(method = "setClearWeatherTime", at = @At("HEAD"), cancellable = true)
    public void setClearWeatherTimeMixin(int clearTime, CallbackInfo ci) {
        WorldEvents.WeatherClearEvent event = new WorldEvents.WeatherClearEvent(clearTime);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) ci.cancel();
    }

    @Inject(method = "setSpawn", at = @At("HEAD"))
    public void setSpawn(BlockPos blockPos, float v, CallbackInfo ci) {
        WorldEvents.SpawnChangeEvent event = new WorldEvents.SpawnChangeEvent(blockPos, null);  // TODO: Get the level this happened in
        Conduit.getEventManager().dispatchEvent(event);
    }
}
