package systems.conduit.core.datastore

import java.util.*

/**
 * The abstract type that different backends must extend to be valid datastore backends.
 *
 * @author Innectic
 * @since 12/30/2019
 */
interface DatastoreHandler {
    /**
     * Attach to the datastore's storage location.
     * This can be a file, memory, databases, or anything the heart desires!
     */
    fun attach(meta: Map<String, Any>)

    /**
     * Detach from the storage location.
     */
    fun detach()

    /**
     * Set a string value in the datastore.
     *
     * @param key   the name of the entry to set
     * @param value the new value of the key
     */
    operator fun set(key: String, value: String)

    /**
     * Set an integer value in the datastore.
     *
     * @param key   the name of the entry to set
     * @param value the new value of the key
     */
    operator fun set(key: String, value: Int)

    /**
     * Set a float value in the datastore.
     *
     * @param key   the name of the entry to set
     * @param value the new value of the key
     */
    operator fun set(key: String, value: Float)

    /**
     * Set a double value in the datastore.
     *
     * @param key   the name of the entry to set
     * @param value the new value of the key
     */
    operator fun set(key: String, value: Double)

    /**
     * Set a custom value in the datastore.
     *
     * @param key   the name of the entry to set
     * @param value the new value of the key
     */
    operator fun set(key: String, value: Storable<*>)

    /**
     * Get an integer from the datastore.
     *
     * @param key the name of the entry
     * @return    the value, if any
     */
    fun getInt(key: String): Int?

    /**
     * Get a float from the datastore.
     *
     * @param key the name of the entry
     * @return    the value, if any
     */
    fun getFloat(key: String): Float?

    /**
     * Get a double from the datastore.
     *
     * @param key the name of the entry
     * @return    the value, if any
     */
    fun getDouble(key: String): Double?

    /**
     * Get a String from the datastore.
     *
     * @param key the name of the entry
     * @return    the value, if any
     */
    fun getString(key: String): String?

    /**
     * Get a custom object from the datastore.
     *
     * @param key the name of the entry
     * @return    the value, if any
     */
    fun <T> getCustom(key: String): Storable<T>?

    /**
     * Delete a key from the datastore.
     *
     * @param key the key to delete
     */
    fun delete(key: String)
}