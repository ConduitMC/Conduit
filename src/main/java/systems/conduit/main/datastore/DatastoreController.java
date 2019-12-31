package systems.conduit.main.datastore;

import lombok.RequiredArgsConstructor;
import systems.conduit.main.Conduit;
import systems.conduit.main.plugin.Plugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Innectic
 * @since 12/31/2019
 */
@RequiredArgsConstructor
public class DatastoreController {

    private Map<String, DatastoreHandler> handlers = new HashMap<>();

    private String convertName(String name) {
        return name.replace("\\", "").replace("\"", "").replace("'", "").replace(" ", "_").replace(";", "").replace(":", "");
    }

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
    public void registerDatastore(Plugin plugin, String name, Map<String, Object> meta, DatastoreBackend backend) {
        // Start by converting the name into something database friendly.
        name = convertName(name);
        meta.put("table", convertName(plugin.getMeta().name() + "-" + name));

        // Once we have converted that, we need to make a new instance of the selected handler that we will be using for this store.
        Optional<DatastoreHandler> handler = createNewHandlerInstance(backend.getHandler());
        if (!handler.isPresent()) {
            Conduit.getLogger().error("Failed to instantiate datastore handler!");
            return;
        }
        handler.get().attach(meta);

        this.handlers.put(name, handler.get());
    }

    public void closeDatastore(String name) {
        name = convertName(name);

        DatastoreHandler handler = handlers.getOrDefault(name, null);
        if (handler == null) return;

        handler.detach();
        this.handlers.remove(name);
    }
}
