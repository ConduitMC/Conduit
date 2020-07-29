package systems.conduit.main.datastore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.conduit.main.datastore.backend.MemoryBackend;
import systems.conduit.main.datastore.backend.MySQLBackend;

/**
 * The type of backend the datastore uses.
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
