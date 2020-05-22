package systems.conduit.core.datastore

/**
 * Abstract backend for datastores that can have expiring data.
 *
 * @author Innectic
 * @since 1/3/2020
 */
interface ExpirableBackend {
    /**
     * Set a string value in the datastore that will expire.
     *
     * @param key      the name of the entry to set
     * @param value    the value of the entry
     * @param duration TTL of the new entry
     */
    operator fun set(key: String, value: String, duration: Int)

    /**
     * Set an int value in the datastore that will expire.
     *
     * @param key      the name of the entry to set
     * @param value    the value of the entry
     * @param duration TTL of the new entry
     */
    operator fun set(key: String, value: Int, duration: Int)

    /**
     * Set a float value in the datastore that will expire.
     *
     * @param key      the name of the entry to set
     * @param value    the value of the entry
     * @param duration TTL of the new entry
     */
    operator fun set(key: String, value: Float, duration: Int)

    /**
     * Set a double value in the datastore that will expire.
     *
     * @param key      the name of the entry to set
     * @param value    the value of the entry
     * @param duration TTL of the new entry
     */
    operator fun set(key: String, value: Double, duration: Int)

    /**
     * Set a custom storable value in the datastore that will expire.
     *
     * @param key      the name of the entry to set
     * @param value    the value of the entry
     * @param duration TTL of the new entry
     */
    operator fun set(key: String, value: Storable<*>, duration: Int)
}