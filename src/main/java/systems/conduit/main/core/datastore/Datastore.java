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
     * @param schema the schema to add
     */
    void attachSchema(Class<? extends Schema> schema);

    /**
     * Insert a schema into the datastore
     *
     * @param schema the schema to be serialized
     */
    void insert(Schema schema);

    /**
     * Delete an object from the datastore by the primary key
     *
     * @param primaryKey the primary key to search for and remove
     */
    void delete(Object primaryKey);

    /**
     * Filter the datastore and remove anything that matches
     *
     * @param filter the filter that defines the removal
     */
    void filterAndDelete(Function<Schema, Boolean> filter);

    /**
     * Filter the datastore and return matching schemas
     *
     * @param schema the schema type that should be returned
     * @param filter the rules to match with
     * @return any matching schemas
     */
    List<Schema> filter(Class<? extends Schema> schema, Function<Schema, Boolean> filter);
}
