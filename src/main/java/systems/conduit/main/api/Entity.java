package systems.conduit.main.api;

import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.UUID;

/**
 * Base Conduit interface for use with mixins.
 *
 * @since API 0.1
 *
 * Implementation: {@link systems.conduit.main.mixins.api.EntityMixin}
 */
public interface Entity {

    UUID getUUID();

    double getX();
    double getY();
    double getZ();

    void sendMessage(String message);
    void sendMessage(Component message);

    Level getLevel();

    void teleport(double x, double y, double z);
    void teleport(Position position);
    void teleport(Entity entity);
}
