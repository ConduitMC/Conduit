package systems.conduit.main.mixins.event.world;

import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.Conduit;
import systems.conduit.main.events.types.WorldEvents;

import java.util.Optional;

/**
 * @author Innectic
 * @since 11/25/2020
 */
@Mixin(value = {DerivedLevelData.class, PrimaryLevelData.class}, remap = false)
public abstract class ServerLevelDataMixin implements systems.conduit.main.api.ServerLevelData {

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
}
