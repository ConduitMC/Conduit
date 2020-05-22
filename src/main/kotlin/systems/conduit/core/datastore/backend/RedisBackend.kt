package systems.conduit.core.datastore.backend

import redis.clients.jedis.Jedis
import systems.conduit.core.datastore.DatastoreHandler
import systems.conduit.core.datastore.ExpirableBackend
import systems.conduit.core.datastore.Storable
import java.util.*

/**
 * An expirable datastore that holds information in a Redis database.
 *
 * @author Innectic
 * @since 1/3/2020
 */
class RedisBackend: DatastoreHandler, ExpirableBackend {

    var client: Jedis? = null
        private set

    override fun attach(meta: Map<String, Any>) {
        client = Jedis(meta["host"] as String, meta["port"] as Int)
        client!!.auth(meta["password"] as String)
    }

    override fun detach() {
        client!!.disconnect()
        client = null
    }

    override fun set(key: String, value: String) {
        client?.set(key, value)
    }

    override fun set(key: String, value: Int) {
        client?.set(key, value.toString())
    }

    override fun set(key: String, value: Float) {
        client?.set(key, value.toString())
    }

    override fun set(key: String, value: Double) {
        client?.set(key, value.toString())
    }

    override fun set(key: String, value: Storable<*>) {
        client?.set(key, value.toString())
    }

    override fun set(key: String, value: String, duration: Int) {
        client?.setex(key, duration, value)
    }

    override fun set(key: String, value: Int, duration: Int) {
        client?.setex(key, duration, value.toString())
    }

    override fun set(key: String, value: Float, duration: Int) {
        client?.setex(key, duration, value.toString())
    }

    override fun set(key: String, value: Double, duration: Int) {
        client?.setex(key, duration, value.toString())
    }

    override fun set(key: String, value: Storable<*>, duration: Int) {
        client?.setex(key, duration, value.serialize())
    }

    override fun getInt(key: String): Int? {
        return try {
            Integer.valueOf(client?.get(key))
        } catch (ignored: NumberFormatException) {
            null
        }
    }

    override fun getFloat(key: String): Float? {
        return try {
            java.lang.Float.valueOf(client?.get(key))
        } catch (ignored: NumberFormatException) {
            null
        }
    }

    override fun getDouble(key: String): Double? {
        return try {
            java.lang.Double.valueOf(client?.get(key))
        } catch (ignored: NumberFormatException) {
            null
        }
    }

    override fun getString(key: String): String? {
        return client?.get(key)
    }

    override fun <T> getCustom(key: String): Storable<T>? {
        return null // TODO: Figure out how this is all going to work because it's confusing
    }

    override fun delete(key: String) {
        client?.del(key)
    }
}
