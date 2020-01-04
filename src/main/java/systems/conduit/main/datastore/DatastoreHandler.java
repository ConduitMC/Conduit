package systems.conduit.main.datastore;

import java.util.Map;
import java.util.Optional;

/**
 * The abstract type that different backends must extend to be valid datastore backends.
 *
 * @author Innectic
 * @since 12/30/2019
 */
public interface DatastoreHandler {

    /**
     * Attach to the datastore's storage location.
     * This can be a file, memory, databases, or anything the heart desires!
     */
    void attach(Map<String, Object> meta);

    /**
     * Detach from the storage location.
     */
    void detach();

    /**
     * Set a string value in the datastore.
     *
     * @param key   the name of the entry to set
     * @param value the new value of the key
     */
    void set(String key, String value);

    /**
     * Set an integer value in the datastore.
     *
     * @param key   the name of the entry to set
     * @param value the new value of the key
     */
    void set(String key, int value);

    /**
     * Set a float value in the datastore.
     *
     * @param key   the name of the entry to set
     * @param value the new value of the key
     */
    void set(String key, float value);

    /**
     * Set a double value in the datastore.
     *
     * @param key   the name of the entry to set
     * @param value the new value of the key
     */
    void set(String key, double value);

    /**
     * Set a custom value in the datastore.
     *
     * @param key   the name of the entry to set
     * @param value the new value of the key
     */
    void set(String key, Storable<?> value);

    /**
     * Get an integer from the datastore.
     *
     * @param key the name of the entry
     * @return    the value, if any
     */
    Optional<Integer> getInt(String key);

    /**
     * Get a float from the datastore.
     *
     * @param key the name of the entry
     * @return    the value, if any
     */
    Optional<Float> getFloat(String key);

    /**
     * Get a double from the datastore.
     *
     * @param key the name of the entry
     * @return    the value, if any
     */
    Optional<Double> getDouble(String key);

    /**
     * Get a String from the datastore.
     *
     * @param key the name of the entry
     * @return    the value, if any
     */
    Optional<String> getString(String key);

    /**
     * Get a custom object from the datastore.
     *
     * @param key the name of the entry
     * @return    the value, if any
     */
    <T> Optional<Storable<T>> getCustom(String key);

    /**
     * Delete a key from the datastore.
     *
     * @param key the key to delete
     */
    void delete(String key);
}
