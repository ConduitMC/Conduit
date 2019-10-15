package systems.conduit.main.plugin;

import lombok.AccessLevel;
import lombok.Getter;
import systems.conduit.main.Conduit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

public class PluginManager {

    @Getter(AccessLevel.PUBLIC) private Queue<Plugin> plugins = new ConcurrentLinkedQueue<>();

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
            loadPlugin(file);
        }
    }

    public void loadPlugin(File file) {
        try (PluginClassLoader classLoader = new PluginClassLoader(file, this.getClass().getClassLoader())) {
            Optional<Plugin> plugin = classLoader.load();
            // The plugin is now loaded, now we can attempt to enable the plugin.
            if (plugin.isPresent()) {
                plugins.add(plugin.get());
                Conduit.pluginManager.enable(plugin.get());
            }
        } catch (IOException e) {
            Conduit.LOGGER.error("Error loading plugin.");
            e.printStackTrace();
        }
    }

    public void disablePlugins() {
        if (plugins.isEmpty()) return;
        Conduit.LOGGER.info("Disabling plugins...");
        // Loop through all plugins and disable them
        Iterator<Plugin> iterator = plugins.iterator();
        while (iterator.hasNext()) {
            // Disable the plugin
            disable(iterator.next());
            // Remove plugin from list
            iterator.remove();
        }
    }

    public void enable(Plugin plugin) {
        Conduit.LOGGER.info("Enabling plugin: " + plugin.getMeta().name());
        plugin.setPluginState(PluginState.ENABLING);
        plugin.onEnable();
        plugin.setPluginState(PluginState.ENABLED);
        Conduit.LOGGER.info("Enabled plugin: " + plugin.getMeta().name());
    }

    public void disable(Plugin plugin) {
        Conduit.LOGGER.info("Disabling plugin: " + plugin.getMeta().name());
        plugin.setPluginState(PluginState.DISABLING);
        plugin.onDisable();
        plugin.setPluginState(PluginState.DISABLED);
        Conduit.LOGGER.info("Disabled plugin: " + plugin.getMeta().name());
    }

    public void reload(Plugin plugin) {
        // Unload the plugin
        disable(plugin);
        AtomicReference<Optional<File>> pluginFile = new AtomicReference<>(Optional.empty());
        // Remove the plugin if found and store the file for later
        plugins.removeIf(entryPlugin -> {
            pluginFile.set(Optional.ofNullable(entryPlugin.getClassLoader().getPluginFile()));
            return entryPlugin.equals(plugin);
        });
        // Load plugin again
        pluginFile.get().ifPresent(Conduit.pluginManager::loadPlugin);
    }

    public Optional<Plugin> getPlugin(String name) {
        return plugins.parallelStream().filter(plugin -> plugin.getMeta().name().equalsIgnoreCase(name)).findFirst();
    }
}
