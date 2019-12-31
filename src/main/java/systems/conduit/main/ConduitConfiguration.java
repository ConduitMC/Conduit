package systems.conduit.main;

import lombok.Getter;
import systems.conduit.main.plugin.config.Configuration;
import systems.conduit.main.plugin.config.annotation.ConfigFile;

/**
 * @author Innectic
 * @since 12/30/2019
 */
@ConfigFile(name = "conduit", type = "json", defaultFile = "conduit.json")
public class ConduitConfiguration extends Configuration {

    @Getter
    private class DatastoreConfiguration {
        private String type;
        private String host;
        private int port;
        private String username;
        private String password;
        private String database;
    }

    @Getter private DatastoreConfiguration datastoreConfiguration;
}