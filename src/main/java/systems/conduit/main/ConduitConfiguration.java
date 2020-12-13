package systems.conduit.main;

import lombok.Getter;
import lombok.NoArgsConstructor;
import systems.conduit.main.core.plugin.config.Configuration;
import systems.conduit.main.core.plugin.config.annotation.ConfigFile;

/**
 * Main configuration file for Conduit.
 *
 * @author Innectic
 * @since 12/30/2019
 */
@ConfigFile(name = "conduit", type = "json", defaultFile = "conduit.json")
@NoArgsConstructor
public class ConduitConfiguration extends Configuration {

    @Getter
    @NoArgsConstructor
    private class MySQLConfiguration {
        private String host;
        private int port;
        private String username;
        private String password;
        private String database;
    }

    @Getter
    @NoArgsConstructor
    private class RedisConfiguration {
        private String host;
        private int port;
        private String password;
        private int db;
    }

    @Getter
    @NoArgsConstructor
    private class DatastoreConfiguration {
        private MySQLConfiguration mysql;
        private RedisConfiguration redis;
    }

    @Getter private DatastoreConfiguration datastores;
}
