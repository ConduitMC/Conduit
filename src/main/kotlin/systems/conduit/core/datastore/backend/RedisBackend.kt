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
    private var client: Jedis? = null
    override fun attach(meta: Map<String?, Any>) {
        client = Jedis(meta["host"] as String?, meta["port"] as Int)
        client!!.auth(meta["password"] as String?)
    }

    override fun detach() {
        client!!.disconnect()
        client = null
    }

    private fun getClient(): Optional<Jedis> {
        return Optional.ofNullable(client)
    }

    override fun set(key: String?, value: String?) {
        getClient().ifPresent { j: Jedis -> j[key] = value }
    }

    override fun set(key: String?, value: Int) {
        getClient().ifPresent { j: Jedis -> j[key] = value.toString() }
    }

    override fun set(key: String?, value: Float) {
        getClient().ifPresent { j: Jedis -> j[key] = value.toString() }
    }

    override fun set(key: String?, value: Double) {
        getClient().ifPresent { j: Jedis -> j[key] = value.toString() }
    }

    override fun set(key: String?, value: Storable<*>) {
        getClient().ifPresent { j: Jedis -> j[key] = value.serialize() }
    }

    override fun set(key: String?, value: String?, duration: Int) {
        getClient().ifPresent { j: Jedis -> j.setex(key, duration, value) }
    }

    override fun set(key: String?, value: Int, duration: Int) {
        getClient().ifPresent { j: Jedis -> j.setex(key, duration, value.toString()) }
    }

    override fun set(key: String?, value: Float, duration: Int) {
        getClient().ifPresent { j: Jedis -> j.setex(key, duration, value.toString()) }
    }

    override fun set(key: String?, value: Double, duration: Int) {
        getClient().ifPresent { j: Jedis -> j.setex(key, duration, value.toString()) }
    }

    override fun set(key: String?, value: Storable<*>, duration: Int) {
        getClient().ifPresent { j: Jedis -> j.setex(key, duration, value.serialize()) }
    }

    override fun getInt(key: String?): Optional<Int?>? {
        return getClient().map { j: Jedis ->
            try {
                return@map Integer.valueOf(j[key])
            } catch (ignored: NumberFormatException) {
                return@map null
            }
        }
    }

    override fun getFloat(key: String?): Optional<Float?>? {
        return getClient().map { j: Jedis ->
            try {
                return@map java.lang.Float.valueOf(j[key])
            } catch (ignored: NumberFormatException) {
                return@map null
            }
        }
    }

    override fun getDouble(key: String?): Optional<Double?>? {
        return getClient().map { j: Jedis ->
            try {
                return@map java.lang.Double.valueOf(j[key])
            } catch (ignored: NumberFormatException) {
                return@map null
            }
        }
    }

    override fun getString(key: String?): Optional<String?> {
        return getClient().map { j: Jedis -> j[key] }
    }

    override fun <T> getCustom(key: String?): Optional<Storable<T>?> {
        return Optional.empty() // TODO: Figure out how this is all going to work because it's confusing
    }

    override fun delete(key: String?) {
        getClient().ifPresent { j: Jedis -> j.del(key) }
    }
}