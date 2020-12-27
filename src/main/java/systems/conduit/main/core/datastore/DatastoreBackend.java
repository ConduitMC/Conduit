package systems.conduit.main.core.datastore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import systems.conduit.main.core.datastore.backend.MySQLBackend;

/**
 * The type of backend the datastore uses.
 *
 * @author Innectic
 * @since 12/30/2019
 */
@AllArgsConstructor
@Getter
public enum DatastoreBackend {
    MySQL(MySQLBackend.class);

    private Class<? extends Datastore> handler;
}
