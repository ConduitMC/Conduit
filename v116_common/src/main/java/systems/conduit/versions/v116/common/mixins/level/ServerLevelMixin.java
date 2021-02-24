package systems.conduit.versions.v116.common.mixins.level;

import lombok.Getter;
import net.minecraft.util.ProgressListener;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.api.mixins.Entity;
import systems.conduit.main.core.api.mixins.ServerLevel;
import systems.conduit.main.core.api.mixins.ServerPlayer;
import systems.conduit.main.core.events.types.WorldEvents;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Innectic
 * @since 10/24/2020
 */
@Mixin(value = net.minecraft.server.level.ServerLevel.class, remap = false)
public abstract class ServerLevelMixin implements ServerLevel {

    @Shadow @Final @Getter private List<ServerPlayer> players;
    @Shadow @Final @Getter private ServerLevelData serverLevelData;

    @Override
    public Optional<Entity> getEntityByUuid(UUID uuid) {
        Entity entity = this.getEntitiesByUuid().get(uuid);
        if (entity == null) return Optional.empty();
        return Optional.of(entity);
    }

    @Inject(method = "save", at = @At("HEAD"))
    public void onLevelSave(ProgressListener progressListener, boolean b, boolean b1, CallbackInfo ci) {
        WorldEvents.WorldSaveEvent event = new WorldEvents.WorldSaveEvent(this);
        Conduit.getEventManager().dispatchEvent(event);
    }

    @Inject(method = "unload", at = @At("HEAD"))
    public void unload(LevelChunk chunk, CallbackInfo callback) {
        WorldEvents.ChunkUnloadEvent event = new WorldEvents.ChunkUnloadEvent(this, chunk);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) callback.cancel();
    }
}
