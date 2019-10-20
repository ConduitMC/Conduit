package systems.conduit.main.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.conduit.main.plugin.Plugin;

/**
 * @author Innectic
 * @since 10/19/2019
 */
public class ServerEvents {

    public static class ServerInitializedEvent extends EventType { }

    public static class ServerStartingEvent extends EventType { }

    public static class ServerShuttingDownEvent extends EventType { }

    @AllArgsConstructor
    @Getter
    public static class PluginReloadEvent extends EventType {
        private Plugin plugin;
    }
}
