package systems.conduit.core.datastore.backend

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import systems.conduit.core.datastore.DatastoreHandler
import systems.conduit.core.datastore.Storable
import java.sql.SQLException
import java.util.*

/**
 * Datastore backend that stores its information in MySQL / SQLite.
 *
 * @author Innectic
 * @since 12/31/2019
 */
class MySQLBackend: DatastoreHandler {

    private val config = HikariConfig()
    var source: HikariDataSource? = null
        private set
    private var table: String? = null

    override fun attach(meta: Map<String, Any>) {
        // TODO: Implement SQLite
        if (!meta.containsKey("host") || !meta.containsKey("port") || !meta.containsKey("database") || !meta.containsKey("username") || !meta.containsKey("password") || !meta.containsKey("table")) return
        table = meta["table"] as String? // TODO: Make sure there's nothing scary in there
        val url = "jdbc:mysql://" + meta["host"] + ":" + meta["port"] + "/" + meta["database"]
        config.jdbcUrl = url
        config.username = meta["username"] as String?
        config.password = meta["password"] as String?
        source = HikariDataSource(config)
    }

    override fun detach() {
        source?.close()
        source = null
    }

    override fun set(key: String, value: String) {
        if (source == null) return
        val conn = source!!.connection ?: return
        val statement = conn.prepareStatement("INSERT INTO $table (key,value) VALUES (?,?) ON DUPLICATE KEY UPDATE value=?") ?: return
        statement.setString(1, key)
        statement.setString(2, value)
        statement.setString(3, value)
        statement.execute()
        statement.close()
    }

    override fun set(key: String, value: Int) {
        set(key, value.toString())
    }

    override fun set(key: String, value: Float) {
        set(key, value.toString())
    }

    override fun set(key: String, value: Double) {
        set(key, value.toString())
    }

    override fun set(key: String, value: Storable<*>) {
        set(key, value.toString())
    }

    override fun getInt(key: String): Int? {
        return try {
            return Integer.valueOf(getString(key))
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            null
        }
    }

    override fun getFloat(key: String): Float? {
        return try {
            return java.lang.Float.valueOf(getString(key))
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            null
        }
    }

    override fun getDouble(key: String): Double? {
        return try {
            return java.lang.Double.valueOf(getString(key))
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            null
        }
    }

    override fun getString(key: String): String? {
        try {
            if (source == null) return null
            val conn = source!!.connection ?: return null
            val statement = conn.prepareStatement("SELECT value FROM $table WHERE key=?") ?: return null
            statement.setString(1, key)
            val results = statement.executeQuery()
            return if (results != null) {
                if (results.next()) {
                    results.close()
                    statement.close()
                    results.getString("value")
                } else {
                    results.close()
                    statement.close()
                    null
                }
            } else {
                null
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            return null
        }
    }

    override fun <T> getCustom(key: String): Storable<T>? {
        return null // TODO: Figure out how this is all going to work because it's confusing
    }

    override fun delete(key: String) {
        try {
            if (source == null) return
            val conn = source!!.connection ?: return
            val statement = conn.prepareStatement("DELETE FROM $table WHERE key=?") ?: return
            statement.setString(1, key)
            statement.execute()
            statement.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}