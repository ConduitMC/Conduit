package systems.conduit.main.api.mixins;

import net.minecraft.world.level.storage.ServerLevelData;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Innectic
 * @since 10/24/2020
 */
public interface ServerLevel extends Level {

    List<ServerPlayer> getPlayers();
    Map<UUID, Entity> getEntitiesByUuid();
    Optional<Entity> getEntityByUuid(UUID uuid);
    ServerLevelData getServerLevelData();
}
