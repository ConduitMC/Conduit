package systems.conduit.core.datastore

/**
 * A custom object that can be stored within a datastore.
 *
 * @author Innectic
 * @since 12/30/2019
 */
interface Storable<T> {
    /**
     * Serialize the object to a storable format.
     *
     * @return the data ready to be stored.
     */
    fun serialize(): String?

    /**
     * Convert the serialized data back into the custom object type.
     *
     * @return the deserialized data.
     */
    fun deserialize(): T
}