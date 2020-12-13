package systems.conduit.main.core.plugin.config;

import systems.conduit.main.Conduit;
import systems.conduit.main.core.plugin.config.loader.JsonLoader;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/*
 * @author Innectic
 * @since 10/17/2019
 */
public class ConfigurationTypes {
    private static final Map<String, ConfigurationLoader> loaderCache = new HashMap<>();
    private static final Map<String, Class<? extends ConfigurationLoader>> loaderTypes = new HashMap<>();

    static {
        loaderTypes.put("json", JsonLoader.class);
    }

    public static Optional<ConfigurationLoader> getLoaderForExtension(String extension) {
        // First, check if we currently have an already created loader cached.
        if (loaderCache.containsKey(extension)) return Optional.of(loaderCache.get(extension));

        // Since it does not currently contain an instance, lets attempt to make one.
        Class<? extends ConfigurationLoader> loaderClass = loaderTypes.getOrDefault(extension, null);
        // Invalid extension provided.
        if (loaderClass == null) return Optional.empty();

        // Valid extension, so we have a class type and can make a new cache loader.
        try {
            ConfigurationLoader loader = loaderClass.getConstructor().newInstance();
            loaderCache.put(extension, loader);
            return Optional.of(loader);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Conduit.getLogger().error("Failed to create instance of configuration loader for " + extension + ":");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static void drop(String extension) {
        loaderCache.remove(extension);
    }
}
