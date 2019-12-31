package systems.conduit.main.datastore;

import java.util.Optional;

/**
 * @author Innectic
 * @since 12/30/2019
 */
public abstract class DatastoreHandler {

    /**
     * Attach to the datastore's storage location.
     *
     * This can be a file, memory, databases, or anything the heart desires!
     */
    public abstract void attach();

    /**
     * Detach from the storage location.
     */
    public abstract void detach();

    /**
     * Set a string value in the datastore
     *
     * @param key   the name of the entry to set
     * @param value the new value of the key
     */
    public abstract void set(String key, String value);

    /**
     * Set an integer value in the datastore
     *
     * @param key   the name of the entry to set
     * @param value the new value of the key
     */
    public abstract void set(String key, int value);

    /**
     * Set a float value in the datastore
     *
     * @param key   the name of the entry to set
     * @param value the new value of the key
     */
    public abstract void set(String key, float value);

    /**
     * Set a double value in the datastore
     *
     * @param key   the name of the entry to set
     * @param value the new value of the key
     */
    public abstract void set(String key, double value);

    /**
     * Set a custom value in the datastore
     *
     * @param key   the name of the entry to set
     * @param value the new value of the key
     */
    public abstract void set(String key, Storable<?> value);

    /**
     * Get an integer from the datastore.
     *
     * @param key the name of the entry
     * @return    the value, if any
     */
    public abstract Optional<Integer> getInt(String key);

    /**
     * Get a float from the datastore.
     *
     * @param key the name of the entry
     * @return    the value, if any
     */
    public abstract Optional<Float> getFloat(String key);

    /**
     * Get a double from the datastore.
     *
     * @param key the name of the entry
     * @return    the value, if any
     */
    public abstract Optional<Double> getDouble(String key);

    /**
     * Get a String from the datastore.
     *
     * @param key the name of the entry
     * @return    the value, if any
     */
    public abstract Optional<String> getString(String key);

    /**
     * Get a custom object from the datastore.
     *
     * @param key the name of the entry
     * @return    the value, if any
     */
    public abstract <T> Optional<Storable<T>> getCustom(String key);
}
