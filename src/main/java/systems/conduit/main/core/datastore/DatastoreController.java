package systems.conduit.main.core.datastore;

import lombok.RequiredArgsConstructor;
import systems.conduit.main.Conduit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Manages a plugin's datastores.
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
     * Create a new instance of a datastore.
     *
     * @param clazz the datastore class to create
     * @return an instance of the datastore ready to be attached, empty if it failed.
     */
    private Optional<DatastoreHandler> createNewHandlerInstance(Class<? extends DatastoreHandler> clazz) {
        try {
            Constructor<? extends DatastoreHandler> constructor = clazz.getConstructor();
            DatastoreHandler handler = constructor.newInstance();
            return Optional.of(handler);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Conduit.getLogger().error("Failed to instantiate datastore handler!");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Register a new datastore.
     *
     * @param name    the identifier for this datastore.
     * @param meta    information to be provided when attaching to the datastore.
     * @param backend the location that this datastore will be storing information
     */
    public void register(String name, Map<String, Object> meta, DatastoreBackend backend) {
        // Make sure the name is valid
        if (isInvalidName(name)) return;
        name = pluginName + "-" + name;
        meta.put("table", name);

        // Once we have converted that, we need to make a new instance of the selected handler that we will be using for this store.
        Optional<DatastoreHandler> handler = createNewHandlerInstance(backend.getHandler());
        if (!handler.isPresent()) {
            Conduit.getLogger().error("Failed to instantiate datastore handler!");
            return;
        }
        handler.get().attach(meta);

        this.handlers.put(name, handler.get());
    }

    /**
     * Delete a datastore and it's information.
     *
     * @param name the name of the datastore to delete
     */
    public void close(String name) {
        if (isInvalidName(name)) return;

        DatastoreHandler handler = handlers.getOrDefault(pluginName + "-" + name, null);
        if (handler == null) return;

        handler.detach();
        this.handlers.remove(name);
    }

    /**
     * Get a plugin's datastore by name.
     *
     * @param name   the name of the datastore
     * @return       the datastore, if present. Empty otherwise.
     */
    public Optional<DatastoreHandler> get(String name) {
        if (isInvalidName(name)) return Optional.empty();
        return Optional.ofNullable(handlers.getOrDefault(pluginName + "-" + name, null));
    }
}
