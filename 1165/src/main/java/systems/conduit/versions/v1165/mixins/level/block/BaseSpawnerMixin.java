package systems.conduit.versions.v1165.mixins.level.block;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BaseSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.events.types.WorldEvents;

/**
 * @author Innectic
 * @since 12/27/2020
 */
@Mixin(value = BaseSpawner.class, remap = false)
public class BaseSpawnerMixin {

    @Redirect(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;tryAddFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)Z"))
    public boolean serverTick(ServerLevel serverLevel, Entity entity) {
        WorldEvents.SpawnerSpawnEvent event = new WorldEvents.SpawnerSpawnEvent((BaseSpawner) (Object) this, (systems.conduit.main.core.api.mixins.ServerLevel) serverLevel, (systems.conduit.main.core.api.mixins.Entity) entity);
        Conduit.getEventManager().dispatchEvent(event);

        return event.isCanceled();
    }
}
