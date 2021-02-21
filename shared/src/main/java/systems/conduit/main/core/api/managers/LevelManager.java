package systems.conduit.main.core.api.managers;

import systems.conduit.main.core.api.factories.LevelDataFactory;
import systems.conduit.main.core.api.mixins.ServerLevel;

import java.util.Optional;

public interface LevelManager {

    Optional<ServerLevel> getLevel(String name);
    Optional<net.minecraft.server.level.ServerLevel> createLevel(LevelDataFactory levelData);
}
