package systems.conduit.main.core.datastore;

/**
 * A custom object that can be stored within a datastore.
 *
 * @author Innectic
 * @since 12/30/2019
 */
public interface Storable<T> {

    /**
     * Serialize the object to a storable format.
     *
     * @return the data ready to be stored.
     */
    String serialize();

    /**
     * Convert the serialized data back into the custom object type.
     *
     * @return the deserialized data.
     */
    T deserialize();
}
