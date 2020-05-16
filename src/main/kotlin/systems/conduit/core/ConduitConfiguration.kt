package systems.conduit.core

import systems.conduit.core.plugin.config.Configuration
import systems.conduit.core.plugin.config.annotation.ConfigFile

/**
 * Main configuration file for Conduit.
 *
 * @author Innectic
 * @since 12/30/2019
 */
@ConfigFile(name = "conduit", type = "json", defaultFile = "conduit.json")
class ConduitConfiguration: Configuration() {

    inner class MySQLConfiguration {
        var host: String? = null
            private set
        var port = 0
            private set
        var username: String? = null
            private set
        var password: String? = null
            private set
        var database: String? = null
            private set
    }

    inner class RedisConfiguration {
        var host: String? = null
            private set
        var port = 0
            private set
        var password: String? = null
            private set
        var db = 0
            private set
    }

    inner class DatastoreConfiguration {
        var mysql: MySQLConfiguration? = null
            private set
        var redis: RedisConfiguration? = null
            private set
    }

    var datastores: DatastoreConfiguration? = null
        private set
}
