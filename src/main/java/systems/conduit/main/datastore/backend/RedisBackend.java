package systems.conduit.main.datastore.backend;

import redis.clients.jedis.Jedis;
import systems.conduit.main.datastore.DatastoreHandler;
import systems.conduit.main.datastore.ExpirableBackend;
import systems.conduit.main.datastore.Storable;

import java.util.Map;
import java.util.Optional;

/**
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

    @Override
    public void set(String key, String value) {
        getClient().ifPresent(j -> j.set(key, value));
    }

    @Override
    public void set(String key, int value) {
        getClient().ifPresent(j -> j.set(key, String.valueOf(value)));
    }

    @Override
    public void set(String key, float value) {
        getClient().ifPresent(j -> j.set(key, String.valueOf(value)));
    }

    @Override
    public void set(String key, double value) {
        getClient().ifPresent(j -> j.set(key, String.valueOf(value)));
    }

    @Override
    public void set(String key, Storable<?> value) {
        getClient().ifPresent(j -> j.set(key, value.serialize()));
    }

    @Override
    public Optional<Integer> getInt(String key) {
        return getClient().map(j -> {
            try {
                return Integer.valueOf(j.get(key));
            } catch (NumberFormatException ignored) {
                return null;
            }
        });
    }

    @Override
    public Optional<Float> getFloat(String key) {
        return getClient().map(j -> {
            try {
                return Float.valueOf(j.get(key));
            } catch (NumberFormatException ignored) {
                return null;
            }
        });
    }

    @Override
    public Optional<Double> getDouble(String key) {
        return getClient().map(j -> {
            try {
                return Double.valueOf(j.get(key));
            } catch (NumberFormatException ignored) {
                return null;
            }
        });
    }

    @Override
    public Optional<String> getString(String key) {
        return getClient().map(j -> j.get(key));
    }

    @Override
    public <T> Optional<Storable<T>> getCustom(String key) {
        return Optional.empty();  // TODO: Figure out how this is all going to work because it's confusing
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public void set(String key, String value, int duration) {
        getClient().ifPresent(j -> j.setex(key, duration, value));
    }

    @Override
    public void set(String key, int value, int duration) {
        getClient().ifPresent(j -> j.setex(key, duration, String.valueOf(value)));
    }

    @Override
    public void set(String key, float value, int duration) {
        getClient().ifPresent(j -> j.setex(key, duration, String.valueOf(value)));
    }

    @Override
    public void set(String key, double value, int duration) {
        getClient().ifPresent(j -> j.setex(key, duration, String.valueOf(value)));
    }

    @Override
    public void set(String key, Storable<?> value, int duration) {
        getClient().ifPresent(j -> j.setex(key, duration, value.serialize()));
    }
}
