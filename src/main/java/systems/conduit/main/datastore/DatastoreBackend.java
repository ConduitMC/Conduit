package systems.conduit.main.datastore;

import lombok.AllArgsConstructor;
import systems.conduit.main.datastore.backend.MemoryBackend;
import systems.conduit.main.datastore.backend.MySQLBackend;

/**
 * @author Innectic
 * @since 12/30/2019
 */
@AllArgsConstructor
public enum DatastoreBackend {
    MySQL(MySQLBackend.class), Memory(MemoryBackend.class);

    private Class<? extends DatastoreHandler> handler;
}
