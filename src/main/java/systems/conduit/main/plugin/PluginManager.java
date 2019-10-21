package systems.conduit.main.plugin;

import javassist.tools.Callback;
import systems.conduit.main.Conduit;
import systems.conduit.main.plugin.annotation.PluginMeta;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class PluginManager {

    private Map<String, Plugin> plugins = new ConcurrentHashMap<>();

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
        List<File> plugins = new ArrayList<>();
        for (File file : pluginFiles) {
            // Skip folders, and non-jars
            if (!file.isFile() || !file.getName().endsWith(".jar")) continue;
            // Since it is a file, and it ends with .jar, we can proceed with attempting to load it.
            plugins.add(file);
        }
        loadPlugins(plugins, false);
    }

    // TODO: Store counter for plugins?
    public void loadPlugins(List<File> pluginFiles, boolean reload) {
        try {
            Map<PluginMeta, PluginClassLoader> loaders = new HashMap<>();
            Map<PluginMeta, File> metas = new HashMap<>();
            for (File pluginFile : pluginFiles) {
                try (PluginClassLoader classLoader = new PluginClassLoader(pluginFile, this.getClass().getClassLoader())) {
                    Optional<PluginMeta> pluginMeta = classLoader.loadMeta();
                    pluginMeta.ifPresent(meta -> loaders.put(meta, classLoader));
                    pluginMeta.ifPresent(meta -> metas.put(meta, pluginFile));
                } catch (IOException e) {
                    Conduit.getLogger().error("Error loading plugin: " + pluginFile.getName());
                    e.printStackTrace();
                }
            }
            while (!metas.isEmpty()) {
                for (Iterator<Map.Entry<PluginMeta, File>> it = metas.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<PluginMeta, File> pl = it.next();
                    if (plugins.keySet().containsAll(Arrays.asList(pl.getKey().dependencies()))) {
                        // Load
                        Optional<Plugin> optionalPlugin = loaders.get(pl.getKey()).load(pl.getKey());
                        if (!optionalPlugin.isPresent()) return;
                        Plugin plugin = optionalPlugin.get();
                        // The plugin is now loaded, now we can attempt to enable the plugin.
                        enable(plugin, reload);
                        this.plugins.put(plugin.getMeta().name(), plugin);
                        loaders.remove(pl.getKey());
                        it.remove();
                    }
                }
            }
        } catch (Exception e) {
            Conduit.getLogger().error("Error loading plugin.");
            e.printStackTrace();
        }
    }

    public void disablePlugins() {
        if (plugins.isEmpty()) return;
        Conduit.getLogger().info("Disabling plugins...");
        // Loop through all plugins and disable them
        Iterator<Plugin> iterator = plugins.values().iterator();
        while (iterator.hasNext()) {
            Plugin plugin = iterator.next();
            // Disable the plugin
            disable(plugin, false);
            // Remove plugin from list
            iterator.remove();
        }
    }

    public void enable(Plugin plugin, boolean reload) {
        // Enable now
        if (!reload) Conduit.getLogger().info("Enabling plugin: " + plugin.getMeta().name());
        plugin.setPluginState(PluginState.ENABLING);
        plugin.onEnable();
        plugin.setPluginState(PluginState.ENABLED);
        if (!reload) Conduit.getLogger().info("Enabled plugin: " + plugin.getMeta().name());
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
        plugins.values().removeIf(entryPlugin -> {
            pluginFile.set(Optional.ofNullable(entryPlugin.getClassLoader().getPluginFile()));
            return entryPlugin.equals(plugin);
        });
        // Load plugin again
        pluginFile.get().ifPresent(file -> loadPlugins(Collections.singletonList(file), true));
        if(!server) Conduit.getLogger().info("Reloaded plugin: " + plugin.getMeta().name());
    }

    public Optional<Plugin> getPlugin(String name) {
        return plugins.values().parallelStream().filter(plugin -> plugin.getMeta().name().equalsIgnoreCase(name)).findFirst();
    }

    public Collection<Plugin> getPlugins() {
        return plugins.values();
    }
}
