package systems.conduit.core.mixins.event

import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import systems.conduit.api.BaseSpawner

/**
 * @author Innectic
 * @since 1/4/2020
 */
@Mixin(value = [net.minecraft.world.level.BaseSpawner::class], remap = false)
abstract class BaseSpawnerMixin: BaseSpawner {

    @Inject(method = ["tick"], at = [At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;finalizeSpawn(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/world/entity/SpawnGroupData;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/entity/SpawnGroupData;")])
    fun tick(ci: CallbackInfo?) {
    }
}