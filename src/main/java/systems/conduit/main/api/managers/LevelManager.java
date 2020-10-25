package systems.conduit.main.api.managers;

import net.minecraft.world.level.dimension.DimensionType;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.ServerLevel;

import java.util.Optional;

public class LevelManager {

    /**
     * Attempt to find a level by a given name.
     *
     * @since API 0.1
     * @param name the name of the level
     * @return a level with the name, if found.
     */
    public Optional<ServerLevel> getLevel(String name) {
        return Conduit.getServer().map(server -> {
            for (net.minecraft.server.level.ServerLevel level : server.getAllLevels()) {
                ServerLevel conduitLevel = (ServerLevel) level;
                if (conduitLevel.getServerLevelData().getLevelName().equals(name)) return conduitLevel;
            }
            return null;
        });
    }

    /**
     * Create a new level with given information.
     *
     * @since API 0.1
     *
     * @param name the name of the level
     * @param dimension dimension metadata
     * @return the created level, if it could be created. Empty otherwise.
     */
    public Optional<ServerLevel> createLevel(String name, DimensionType dimension) {
        return Optional.empty();
    }
}
