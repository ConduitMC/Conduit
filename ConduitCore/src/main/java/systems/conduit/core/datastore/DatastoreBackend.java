package systems.conduit.core.datastore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.conduit.core.datastore.backend.MemoryBackend;
import systems.conduit.core.datastore.backend.MySQLBackend;

/**
 * The type of backend the systems.conduit.core.datastore uses.
 *
 * @author Innectic
 * @since 12/30/2019
 */
@AllArgsConstructor
@Getter
public enum DatastoreBackend {
    MySQL(MySQLBackend.class), Memory(MemoryBackend.class);

    private Class<? extends DatastoreHandler> handler;
}