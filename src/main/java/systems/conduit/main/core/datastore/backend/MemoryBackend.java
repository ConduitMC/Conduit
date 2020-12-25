package systems.conduit.main.core.datastore.backend;

import systems.conduit.main.core.datastore.DatastoreHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * Datastore backend that puts everything in memory.
 *
 * @author Innectic
 * @since 12/30/2019
 */
public class MemoryBackend implements DatastoreHandler {

    private Map<String, Object> storage = new HashMap<>();

    @Override
    public void attach(Map<String, Object> meta) {
        this.storage = new HashMap<>();
    }

    @Override
    public void detach() {
        this.storage = null;
    }
}
