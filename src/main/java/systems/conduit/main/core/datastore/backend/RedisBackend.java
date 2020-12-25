package systems.conduit.main.core.datastore.backend;

import redis.clients.jedis.Jedis;
import systems.conduit.main.core.datastore.DatastoreHandler;
import systems.conduit.main.core.datastore.ExpirableBackend;

import java.util.Map;
import java.util.Optional;

/**
 * An expirable datastore that holds information in a Redis database.
 *
 * @author Innectic
 * @since 1/3/2020
 */
public class RedisBackend implements DatastoreHandler, ExpirableBackend {

    private Jedis client = null;

    @Override
    public void attach(Map<String, Object> meta) {
        client = new Jedis((String) meta.get("host"), (int) meta.get("port"));
        client.auth((String) meta.get("password"));
    }

    @Override
    public void detach() {
        client.disconnect();
        client = null;
    }

    private Optional<Jedis> getClient() {
        return Optional.ofNullable(client);
    }
}
