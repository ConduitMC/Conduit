package systems.conduit.main.datastore;

import lombok.AllArgsConstructor;

/**
 * @author Innectic
 * @since 12/30/2019
 */
@AllArgsConstructor
public enum DatastoreBackend {
    MySQL(null), Memory(null);

    private Class<?> handler;
}
