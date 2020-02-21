package systems.conduit.core.datastore;

/**
 * Abstract backend for datastores that can have expiring data.
 *
 * @author Innectic
 * @since 1/3/2020
 */
public interface ExpirableBackend {

    /**
     * Set a string value in the datastore that will expire.
     *
     * @param key      the name of the entry to set
     * @param value    the value of the entry
     * @param duration TTL of the new entry
     */
    void set(String key, String value, int duration);


    /**
     * Set an int value in the datastore that will expire.
     *
     * @param key      the name of the entry to set
     * @param value    the value of the entry
     * @param duration TTL of the new entry
     */
    void set(String key, int value, int duration);


    /**
     * Set a float value in the datastore that will expire.
     *
     * @param key      the name of the entry to set
     * @param value    the value of the entry
     * @param duration TTL of the new entry
     */
    void set(String key, float value, int duration);


    /**
     * Set a double value in the datastore that will expire.
     *
     * @param key      the name of the entry to set
     * @param value    the value of the entry
     * @param duration TTL of the new entry
     */
    void set(String key, double value, int duration);


    /**
     * Set a custom storable value in the datastore that will expire.
     *
     * @param key      the name of the entry to set
     * @param value    the value of the entry
     * @param duration TTL of the new entry
     */
    void set(String key, Storable<?> value, int duration);
}
