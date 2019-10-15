package systems.conduit.main.api.managers;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelType;
import systems.conduit.main.Conduit;

import java.util.Optional;

/**
 * @author Innectic
 * @since 10/14/2019
 */
public class LevelManager {

    public Optional<Level> getLevel(String name) {
        if (!Conduit.server.isPresent()) return Optional.empty();
        return Optional.empty();
    }

    public Optional<Level> createLevel(String name, LevelType type) {
        return Optional.empty();
    }
}
