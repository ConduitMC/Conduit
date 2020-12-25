package systems.conduit.main.core.datastore;

import java.util.Map;

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
     * Delete a key from the datastore.
     *
     * @param key the key to delete
     */
    void delete(String key);
}
