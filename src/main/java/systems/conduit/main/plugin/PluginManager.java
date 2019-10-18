package systems.conduit.main.plugin;

import javassist.tools.Callback;
import lombok.AccessLevel;
import lombok.Getter;
import systems.conduit.main.Conduit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

public class PluginManager {

    @Getter(AccessLevel.PUBLIC) private Queue<Plugin> plugins = new ConcurrentLinkedQueue<>();

    /**
     * Finds all classes in runtime that extend {@link Plugin} and are annotated with {@link systems.conduit.main.plugin.annotation.PluginMeta}
     * and attempts to load it as a plugin.
     */
    public void loadPlugins() {
        Conduit.getLogger().info("Loading plugins...");
        // We can now get all the jars in the plugins folder and load all of them.
        File pluginsFolder = Paths.get("plugins").toFile();
        if (!pluginsFolder.exists() && !pluginsFolder.mkdirs()) {
            Conduit.getLogger().error("Failed to make plugins directory.");
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
            loadPlugin(file, false);
        }
    }

    public void loadPlugin(File file, boolean reload) {
        try (PluginClassLoader classLoader = new PluginClassLoader(file, this.getClass().getClassLoader())) {
            Optional<Plugin> plugin = classLoader.load();
            // The plugin is now loaded, now we can attempt to enable the plugin.
            if (plugin.isPresent()) {
                plugins.add(plugin.get());
                enable(plugin.get(), reload);
            }
        } catch (IOException e) {
            Conduit.getLogger().error("Error loading plugin.");
            e.printStackTrace();
        }
    }

    public void disablePlugins() {
        if (plugins.isEmpty()) return;
        Conduit.getLogger().info("Disabling plugins...");
        // Loop through all plugins and disable them
        Iterator<Plugin> iterator = plugins.iterator();
        while (iterator.hasNext()) {
            Plugin plugin = iterator.next();
            // Disable the plugin
            disable(plugin, false);
            // Remove plugin from list
            iterator.remove();
        }
    }

    public void disable(Plugin plugin, boolean server) {
        if ((plugin.getPluginState() == PluginState.DISABLING || plugin.getPluginState() == PluginState.DISABLED)) return;
        if (!server) Conduit.getLogger().info("Disabling plugin: " + plugin.getMeta().name());
        plugin.setPluginState(PluginState.DISABLING);
        plugin.getEvents().clear();
        plugin.onDisable();
        plugin.setPluginState(PluginState.DISABLED);
        if (!server) Conduit.getLogger().info("Disabled plugin: " + plugin.getMeta().name());
    }

    public void enable(Plugin plugin, boolean server) {
        if (plugin.getPluginState() == PluginState.ENABLING || plugin.getPluginState() == PluginState.ENABLED) return;
        if (!server) Conduit.getLogger().info("Enabling plugin: " + plugin.getMeta().name());
        plugin.setPluginState(PluginState.ENABLING);
        plugin.onEnable();
        plugin.setPluginState(PluginState.ENABLED);
        if (!server) Conduit.getLogger().info("Enabled plugin: " + plugin.getMeta().name());
    }

    public void reloadPlugins(boolean server, Callback callback) {
        if(!server) Conduit.getLogger().info("Reloading all plugins...");
        getPlugins().forEach(plugin -> reload(plugin, false));
        callback.result(new Object[]{});
        if(!server) Conduit.getLogger().info("Reloaded all plugins");
    }

    public void reload(Plugin plugin, boolean server) {
        if(!server) Conduit.getLogger().info("Reloading plugin: " + plugin.getMeta().name());
        // Unload the plugin
        disable(plugin, true);
        AtomicReference<Optional<File>> pluginFile = new AtomicReference<>(Optional.empty());
        // Remove the plugin if found and store the file for later
        plugins.removeIf(entryPlugin -> {
            pluginFile.set(Optional.ofNullable(entryPlugin.getClassLoader().getPluginFile()));
            return entryPlugin.equals(plugin);
        });
        // Load plugin again
        pluginFile.get().ifPresent(file -> loadPlugin(file, true));
        if(!server) Conduit.getLogger().info("Reloaded plugin: " + plugin.getMeta().name());
    }

    public Optional<Plugin> getPlugin(String name) {
        return plugins.parallelStream().filter(plugin -> plugin.getMeta().name().equalsIgnoreCase(name)).findFirst();
    }
}
