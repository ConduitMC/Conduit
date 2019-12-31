package systems.conduit.main.datastore.backend;

import systems.conduit.main.datastore.DatastoreHandler;
import systems.conduit.main.datastore.Storable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Innectic
 * @since 12/30/2019
 */
public class MemoryBackend extends DatastoreHandler {

    private Map<String, Object> storage = new HashMap<>();

    @Override
    public void attach() {
        this.storage = new HashMap<>();
    }

    @Override
    public void detach() {
        this.storage = null;
    }

    @Override
    public void set(String key, String value) {
        this.storage.put(key, value);
    }

    @Override
    public void set(String key, int value) {
        this.storage.put(key, value);
    }

    @Override
    public void set(String key, float value) {
        this.storage.put(key, value);
    }

    @Override
    public void set(String key, double value) {
        this.storage.put(key, value);
    }

    @Override
    public void set(String key, Storable<?> value) {
        this.storage.put(key, value);
    }

    @Override
    public Optional<Integer> getInt(String key) {
        Object object = this.storage.get(key);
        if (object == null) return Optional.empty();

        try {
            return Optional.of((Integer) object);
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Float> getFloat(String key) {
        Object object = this.storage.get(key);
        if (object == null) return Optional.empty();

        try {
            return Optional.of((Float) object);
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Double> getDouble(String key) {
        Object object = this.storage.get(key);
        if (object == null) return Optional.empty();

        try {
            return Optional.of((Double) object);
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<String> getString(String key) {
        Object object = this.storage.get(key);
        if (object == null) return Optional.empty();

        try {
            return Optional.of((String) object);
        } catch (ClassCastException e) {
            return Optional.empty();
        }
    }

    @Override
    public <T> Storable<T> getCustom(String key) {
        Object object = this.storage.get(key);
        if (!(object instanceof Storable)) return null;

        try {
            return (Storable<T>) object;
        } catch (ClassCastException e) {
            return null;
        }
    }
}
