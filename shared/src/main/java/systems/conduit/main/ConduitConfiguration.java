package systems.conduit.main;

import lombok.Getter;
import lombok.NoArgsConstructor;
import systems.conduit.main.core.plugin.config.Configuration;
import systems.conduit.main.core.plugin.config.annotation.ConfigFile;

import java.util.HashMap;
import java.util.Map;

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
    public class MySQLConfiguration {
        private String host;
        private int port;
        private String username;
        private String password;

        public Map<String, Object> toMeta() {
            Map<String, Object> meta = new HashMap<>();

            meta.put("host", host);
            meta.put("port", port);
            meta.put("username", username);
            meta.put("password", password);

            return meta;
        }
    }

    @Getter
    @NoArgsConstructor
    public class RedisConfiguration {
        private String host;
        private int port;
        private String password;
        private int db;
    }

    @Getter
    @NoArgsConstructor
    public class DatastoreConfiguration {
        private MySQLConfiguration mysql;
        private RedisConfiguration redis;
    }

    @Getter private DatastoreConfiguration datastores;
}
