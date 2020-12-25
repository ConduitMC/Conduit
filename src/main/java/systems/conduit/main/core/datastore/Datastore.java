package systems.conduit.main.core.datastore;

import systems.conduit.main.core.datastore.schema.Schema;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * The abstract type that different backends must extend to be valid datastore backends.
 *
 * @author Innectic
 * @since 12/30/2019
 */
public interface Datastore {

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
     * Register a new schema in the datastore.
     *
     * @param name the name that should be used internally for this schema
     * @param schema the schema to add
     */
    void attachSchema(String name, Class<? extends Schema> schema);

    /**
     * Get a registered schema
     *
     * @param schema the schema type to find
     */
    void getAttachedSchema(Class<? extends Schema> schema);

    void insert(Class<? extends Schema> schema);

    void delete(Object primaryKey);

    List<Schema> filter(Class<? extends Schema> schema, Function<Schema, Boolean> filter);
}
