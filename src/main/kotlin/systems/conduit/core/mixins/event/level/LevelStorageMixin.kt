package systems.conduit.core.mixins.event.level

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.storage.LevelData
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import systems.conduit.api.LevelStorage
import systems.conduit.core.Conduit
import systems.conduit.core.events.types.WorldEvents

/**
 * @author Innectic
 * @since 1/5/2020
 */
@Mixin(value = [net.minecraft.world.level.storage.LevelStorage::class], remap = false)
abstract class LevelStorageMixin: LevelStorage {

    @Inject(method = ["saveLevelData(Lnet/minecraft/world/level/storage/LevelData;Lnet/minecraft/nbt/CompoundTag;)V"], at = [At("HEAD")])
    fun saveLevelData(levelData: LevelData, compoundTag: CompoundTag?, ci: CallbackInfo?) {
        val level = Conduit.server.getLevels().values.stream().filter { it?.levelData?.levelName.equals(levelData.levelName) }.findFirst()
        if (level.isPresent) {
            val event = WorldEvents.WorldSaveEvent(level.get())
            Conduit.eventManager.dispatchEvent(event)
        }
    }
}
