package systems.conduit.core.datastore

import systems.conduit.core.datastore.backend.MemoryBackend
import systems.conduit.core.datastore.backend.MySQLBackend

/**
 * The type of backend the datastore uses.
 *
 * @author Innectic
 * @since 12/30/2019
 */
enum class DatastoreBackend(val handler: Class<out DatastoreHandler>) {
    MySQL(MySQLBackend::class.java), Memory(MemoryBackend::class.java);
}
