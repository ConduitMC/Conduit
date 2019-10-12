package systems.conduit.main.api;

import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.UUID;

/**
 * @author Innectic
 * @since 10/12/2019
 */
public interface Entity {

    UUID getUUID();

    void sendMessage(String message);
    void sendMessage(Component message);

    Level getLevel();
}
