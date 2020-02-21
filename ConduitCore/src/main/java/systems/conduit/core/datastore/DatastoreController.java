package systems.conduit.core.datastore;

import lombok.RequiredArgsConstructor;
import systems.conduit.core.Conduit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages a systems.conduit.core.plugin's datastores.
 *
 * @author Innectic
 * @since 12/31/2019
 */
@RequiredArgsConstructor
public class DatastoreController {

    private Map<String, DatastoreHandler> handlers = new HashMap<>();
    private final String pluginName;

    /**
     * Make sure that the name does not contain any invalid characters.
     *
     * @param name the name to check
     * @return if the name is valid or not.
     */
    private boolean isInvalidName(String name) {
        return !name.chars().allMatch(v -> Character.isLetterOrDigit(v) || v == '_');
    }

    /**
     * Create a new instance of a systems.conduit.core.datastore.
     *
     * @param clazz the systems.conduit.core.datastore class to create
     * @return an instance of the systems.conduit.core.datastore ready to be attached, empty if it failed.
     */
    private Optional<DatastoreHandler> createNewHandlerInstance(Class<? extends DatastoreHandler> clazz) {
        try {
            Constructor<? extends DatastoreHandler> constructor = clazz.getConstructor();
            DatastoreHandler handler = constructor.newInstance();
            return Optional.of(handler);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Conduit.getLogger().error("Failed to instantiate systems.conduit.core.datastore handler!");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Register a new systems.conduit.core.datastore.
     *
     * @param name    the identifier for this systems.conduit.core.datastore.
     * @param meta    information to be provided when attaching to the systems.conduit.core.datastore.
     * @param backend the location that this systems.conduit.core.datastore will be storing information
     */
    public void register(String name, Map<String, Object> meta, DatastoreBackend backend) {
        // Make sure the name is valid
        if (isInvalidName(name)) return;
        name = pluginName + "-" + name;
        meta.put("table", name);

        // Once we have converted that, we need to make a new instance of the selected handler that we will be using for this store.
        Optional<DatastoreHandler> handler = createNewHandlerInstance(backend.getHandler());
        if (!handler.isPresent()) {
            Conduit.getLogger().error("Failed to instantiate systems.conduit.core.datastore handler!");
            return;
        }
        handler.get().attach(meta);

        this.handlers.put(name, handler.get());
    }

    /**
     * Delete a systems.conduit.core.datastore and it's information.
     *
     * @param name the name of the systems.conduit.core.datastore to delete
     */
    public void close(String name) {
        if (isInvalidName(name)) return;

        DatastoreHandler handler = handlers.getOrDefault(pluginName + "-" + name, null);
        if (handler == null) return;

        handler.detach();
        this.handlers.remove(name);
    }

    /**
     * Get a systems.conduit.core.plugin's systems.conduit.core.datastore by name.
     *
     * @param name   the name of the systems.conduit.core.datastore
     * @return       the systems.conduit.core.datastore, if present. Empty otherwise.
     */
    public Optional<DatastoreHandler> get(String name) {
        if (isInvalidName(name)) return Optional.empty();
        return Optional.ofNullable(handlers.getOrDefault(pluginName + "-" + name, null));
    }
}
