package systems.conduit.main.core.events.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ServerStatus;
import systems.conduit.main.core.plugin.Plugin;

/*
 * @author Innectic
 * @since 10/19/2019
 */
public class ServerEvents {

    public static class ServerInitializedEvent extends EventType {
    }

    public static class ServerStartingEvent extends EventType {
    }

    public static class ServerShuttingDownEvent extends EventType {
    }

    @Getter
    @AllArgsConstructor
    public static class PluginReloadEvent extends EventType {
        private Plugin plugin;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ServerStatusRequestEvent extends EventType {
        private Component motd;
        private ServerStatus.Players onlinePlayers;
        private ServerStatus.Version serverVersion;
        private String serverIcon;

        public static ServerStatusRequestEvent of(ServerStatus status) {
            return new ServerStatusRequestEvent(status.getDescription(), status.getPlayers(), status.getVersion(), status.getFavicon());
        }
    }
}
