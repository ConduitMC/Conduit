package systems.conduit.main.core.datastore.backend;

import systems.conduit.main.core.datastore.Datastore;
import systems.conduit.main.core.datastore.schema.Schema;
import systems.conduit.main.core.datastore.schema.utils.DatastoreUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Innectic
 * @since 1/1/2021
 */
public class MemoryBackend implements Datastore {

    private Map<String, Map<Integer, Schema>> internal;

    @Override
    public void attach(Map<String, Object> meta) {
        internal = new HashMap<>();
    }

    @Override
    public void detach() {
        internal.clear();
    }

    @Override
    public void attachSchema(Class<? extends Schema> schema) {
        String name = DatastoreUtils.getNameOfSchema(schema);
        if (!internal.containsKey(name)) internal.put(name, new HashMap<>());
    }

    @Override
    public void insert(Schema schema) {
        String name = DatastoreUtils.getNameOfSchema(schema.getClass());
        if (!internal.containsKey(name)) return;

        internal.get(name).put(schema.getId(), schema);
    }

    @Override
    public void delete(Class<? extends Schema> schema, String key, Object value) {
        Map<String, Object> filter = new HashMap<>();
        filter.put(key, value);

        filterAndDelete(schema, filter);
    }

    @Override
    public void filterAndDelete(Class<? extends Schema> schema, Map<String, Object> filter) {
        String name = DatastoreUtils.getNameOfSchema(schema);
        if (!internal.containsKey(name)) return;
    }

    @Override
    public List<Schema> filter(Class<? extends Schema> schema, Map<String, Object> filter) {
        return null;
    }
}
