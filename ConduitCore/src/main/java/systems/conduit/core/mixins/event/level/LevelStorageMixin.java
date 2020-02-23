package systems.conduit.core.mixins.event.level;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.storage.LevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.core.Conduit;
import systems.conduit.api.LevelStorage;
import systems.conduit.core.events.types.WorldEvents;

import java.util.Optional;

/**
 * @author Innectic
 * @since 1/5/2020
 */
@Mixin(value = net.minecraft.world.level.storage.LevelStorage.class, remap = false)
public abstract class LevelStorageMixin implements LevelStorage {

    @Inject(method = "saveLevelData(Lnet/minecraft/world/level/storage/LevelData;Lnet/minecraft/nbt/CompoundTag;)V", at = @At("HEAD"))
    public void saveLevelData(LevelData levelData, CompoundTag compoundTag, CallbackInfo ci) {
        WorldEvents.WorldSaveEvent event = new WorldEvents.WorldSaveEvent(Conduit.getServer()
                        .map(s -> s.getLevels().values().stream().filter(l -> l.getLevelData().getLevelName().equals(levelData.getLevelName())).findFirst())
                        .filter(Optional::isPresent).map(Optional::get));
        Conduit.getEventManager().dispatchEvent(event);
    }
}
