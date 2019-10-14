package systems.conduit.main.plugin;

import systems.conduit.main.Conduit;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

public class PluginLoader {

    /**
     * Finds all classes in runtime that extend {@link Plugin} and are annotated with {@link systems.conduit.main.plugin.annotation.PluginMeta}
     * and attempts to load it as a plugin.
     */
    public void loadPlugins() {
        // We can now get all the jars in the plugins folder and load all of them.
        File pluginsFolder = Paths.get("plugins").toFile();
        if (!pluginsFolder.exists() && !pluginsFolder.mkdirs()) {
            Conduit.LOGGER.error("Failed to make plugins directory.");
            return;
        }
        File[] pluginFiles = pluginsFolder.listFiles();
        if (pluginFiles == null) return;
        for (File file : pluginFiles) {
            // Skip folders
            if (!file.isFile()) continue;
            // Make sure that it ends with .jar
            if (!file.getName().endsWith(".jar")) continue;
            // Since it is a file, and it ends with .jar, we can proceed with attempting to load it.
            try {
                PluginClassLoader classLoader = new PluginClassLoader(file, this.getClass().getClassLoader());
                Optional<Plugin> plugin = classLoader.load();
                // The plugin is now loaded, put it into the registry.
                if (plugin.isPresent()) {
                    Conduit.pluginRegistry.registerPlugin(plugin.get(), classLoader);
                    Conduit.LOGGER.info("Loaded plugin: " + plugin.get().getMeta().name());
                }
            } catch (MalformedURLException e) {
                Conduit.LOGGER.error("Error loading plugin.");
                e.printStackTrace();
            }
        }
    }

    public void disablePlugins() {
        if (Conduit.pluginRegistry.getPlugins().isEmpty()) return;
        Conduit.LOGGER.info("Disabling plugins...");
        // Loop through all plugins and disable them
        Iterator<Map.Entry<Plugin, PluginClassLoader> > iterator = Conduit.pluginRegistry.getPlugins().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Plugin, PluginClassLoader> plugin = iterator.next();
            // Disable the plugin
            plugin.getKey().setPluginState(PluginState.DISABLING);
            plugin.getKey().onDisable();
            plugin.getKey().setPluginState(PluginState.DISABLED);
            // Remove plugin from list
            iterator.remove();
            Conduit.LOGGER.info("Disabled plugin: " + plugin.getKey());
        }
    }
}
