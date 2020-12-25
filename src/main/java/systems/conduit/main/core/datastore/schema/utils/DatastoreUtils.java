package systems.conduit.main.core.datastore.schema.utils;

import systems.conduit.main.Conduit;
import systems.conduit.main.core.datastore.Datastore;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * Manages a plugin's datastores.
 *
 * @author Innectic
 * @since 12/31/2019
 */
public class DatastoreUtils {

    /**
     * Make sure that the name does not contain any invalid characters.
     *
     * @param name the name to check
     * @return if the name is valid or not.
     */
    private static boolean isInvalidName(String name) {
        return !name.chars().allMatch(v -> Character.isLetterOrDigit(v) || v == '_');
    }

    /**
     * Create a new instance of a datastore.
     *
     * @param clazz the datastore class to create
     * @return an instance of the datastore ready to be attached, empty if it failed.
     */
    public static Optional<Datastore> createNewHandlerInstance(Class<? extends Datastore> clazz) {
        try {
            Constructor<? extends Datastore> constructor = clazz.getConstructor();
            Datastore handler = constructor.newInstance();
            return Optional.of(handler);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Conduit.getLogger().error("Failed to instantiate datastore handler!");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    //    public void register(String name, Map<String, Object> meta, DatastoreBackend backend) {
//        // Make sure the name is valid
//        if (isInvalidName(name)) return;
//        name = pluginName + "-" + name;
//        meta.put("table", name);
//
//        // Once we have converted that, we need to make a new instance of the selected handler that we will be using for this store.
//        Optional<Datastore> handler = createNewHandlerInstance(backend.getHandler());
//        if (!handler.isPresent()) {
//            Conduit.getLogger().error("Failed to instantiate datastore handler!");
//            return;
//        }
//        handler.get().attach(meta);
//
//        this.handlers.put(name, handler.get());
//    }

//    public void close(String name) {
//        if (isInvalidName(name)) return;
//
//        Datastore handler = handlers.getOrDefault(pluginName + "-" + name, null);
//        if (handler == null) return;
//
//        handler.detach();
//        this.handlers.remove(name);
//    }

//    public Optional<Datastore> get(String name) {
//        if (isInvalidName(name)) return Optional.empty();
//        return Optional.ofNullable(handlers.getOrDefault(pluginName + "-" + name, null));
//    }
}
