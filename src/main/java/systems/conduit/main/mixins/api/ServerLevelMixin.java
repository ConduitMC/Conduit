package systems.conduit.main.mixins.api;

import lombok.Getter;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import systems.conduit.main.api.Entity;
import systems.conduit.main.api.ServerLevel;
import systems.conduit.main.api.ServerPlayer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Innectic
 * @since 10/24/2020
 */
@Mixin(value = net.minecraft.server.level.ServerLevel.class, remap = false)
public class ServerLevelMixin implements ServerLevel {

    @Shadow @Final @Getter private List<ServerPlayer> players;
    @Shadow @Final @Getter private Map<UUID, Entity> entitiesByUuid;
    @Shadow @Final @Getter private ServerLevelData serverLevelData;

    @Override
    public Optional<Entity> getEntityByUuid(UUID uuid) {
        return Optional.ofNullable(entitiesByUuid.getOrDefault(uuid, null));
    }
}
