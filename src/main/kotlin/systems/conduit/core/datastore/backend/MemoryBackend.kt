package systems.conduit.core.datastore.backend

import systems.conduit.core.datastore.DatastoreHandler
import systems.conduit.core.datastore.Storable
import java.util.*

/**
 * Datastore backend that puts everything in memory.
 *
 * @author Innectic
 * @since 12/30/2019
 */
class MemoryBackend: DatastoreHandler {

    private var storage: MutableMap<String, Any> = HashMap()

    override fun attach(meta: Map<String, Any>) {
        storage = HashMap()
    }

    override fun detach() {
        storage = hashMapOf()
    }

    override fun set(key: String, value: String) {
        storage[key] = value
    }

    override fun set(key: String, value: Int) {
        storage[key] = value
    }

    override fun set(key: String, value: Float) {
        storage[key] = value
    }

    override fun set(key: String, value: Double) {
        storage[key] = value
    }

    override fun set(key: String, value: Storable<*>) {
        storage[key] = value
    }

    override fun getInt(key: String): Int? {
        val `object` = storage[key] ?: return null
        return try {
            `object` as Int
        } catch (e: ClassCastException) {
            null
        }
    }

    override fun getFloat(key: String): Float? {
        val `object` = storage[key] ?: return null
        return try {
            `object` as Float
        } catch (e: ClassCastException) {
            null
        }
    }

    override fun getDouble(key: String): Double? {
        val `object` = storage[key] ?: return null
        return try {
            `object` as Double
        } catch (e: ClassCastException) {
            null
        }
    }

    override fun getString(key: String): String? {
        val `object` = storage[key] ?: return null
        return try {
            `object` as String
        } catch (e: ClassCastException) {
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getCustom(key: String): Storable<T>? {
        val `object` = (storage[key] ?: return null) as? Storable<*> ?: return null
        return try {
            `object` as Storable<T>
        } catch (e: ClassCastException) {
            null
        }
    }

    override fun delete(key: String) {
        storage.remove(key)
    }
}
