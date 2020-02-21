package systems.conduit.core.plugin;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import javassist.tools.Callback;
import lombok.Getter;
import systems.conduit.core.Conduit;
import systems.conduit.core.events.types.ServerEvents;
import systems.conduit.core.plugin.annotation.Dependency;
import systems.conduit.core.plugin.annotation.DependencyType;
import systems.conduit.core.plugin.annotation.PluginMeta;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class PluginManager {

    @Getter private Queue<Plugin> plugins = new ConcurrentLinkedQueue<>();

    /**
     * Finds all classes in runtime that extend {@link Plugin} and are annotated with {@link systems.conduit.core.plugin.annotation.PluginMeta}
     * and attempts to load it as a systems.conduit.core.plugin.
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

    private void loadPlugins(List<File> pluginFiles, boolean reload) {
        try {
            // List that should never be changed unless systems.conduit.core.plugin is unable to ever be loaded.
            List<PluginMeta> metas = new ArrayList<>();
            // Loaders - Should be able to be removed from here if we loaded the systems.conduit.core.plugin as systems.conduit.core.plugin now has classloader on it.
            Map<PluginMeta, PluginClassLoader> loaders = new HashMap<>();
            // Needed to be loaded first soft dependencies - Remove systems.conduit.core.plugin from map and any lists
            Multimap<PluginMeta, String> softDependencies = ArrayListMultimap.create();

            for (File pluginFile : pluginFiles) {
                try (PluginClassLoader classLoader = new PluginClassLoader(pluginFile, this.getClass().getClassLoader())) {
                    Optional<PluginMeta> pluginMeta = classLoader.loadMeta();
                    pluginMeta.ifPresent(meta -> loaders.put(meta, classLoader));
                    pluginMeta.ifPresent(metas::add);
                } catch (IOException e) {
                    Conduit.getLogger().error("Error loading systems.conduit.core.plugin: " + pluginFile.getName());
                    e.printStackTrace();
                }
            }
            for (PluginMeta meta : loaders.keySet()) {
                // All soft dependencies
                List<PluginMeta> otherDependencies = metas.stream().filter(meta1 -> !meta1.equals(meta)).collect(Collectors.toList());
                softDependencies.putAll(meta, getSoftDependencies(otherDependencies, meta));
            }
            while (!loaders.isEmpty()) {
                for (Iterator<Map.Entry<PluginMeta, PluginClassLoader>> it = loaders.entrySet().iterator(); it.hasNext();) {
                    Map.Entry<PluginMeta, PluginClassLoader> pl = it.next();
                    List<String> hard = getHardDependencies(pl.getKey());
                    // Check to make sure all hard dependencies are able to be loaded
                    if (!getNames(metas).containsAll(getHardDependencies(pl.getKey()))) {
                        Conduit.getLogger().error("Unable to load systems.conduit.core.plugin: " + pl.getKey().name());
                        hard.removeAll(getNames(metas));
                        Conduit.getLogger().error("Missing needed dependencies: " + hard);
                        // Can't load ever so remove all reference to systems.conduit.core.plugin
                        softDependencies.asMap().remove(pl.getKey());
                        metas.remove(pl.getKey());
                        it.remove();
                        return;
                    }
                    // Load soft soft dependencies first
                    if (softDependencies.get(pl.getKey()).isEmpty()) {
                        Optional<Plugin> optionalPlugin = loaders.get(pl.getKey()).load(pl.getKey());
                        // The systems.conduit.core.plugin should now be loaded, now we can attempt to enable the systems.conduit.core.plugin.
                        if (!optionalPlugin.isPresent()) return;
                        Plugin plugin = optionalPlugin.get();
                        enable(plugin, reload);
                        this.plugins.add(plugin);
                        softDependencies.values().remove(plugin.getMeta().name());
                        softDependencies.asMap().remove(plugin.getMeta());
                        it.remove();
                    }
                }
            }
        } catch (Exception e) {
            Conduit.getLogger().error("Error loading systems.conduit.core.plugin.");
            e.printStackTrace();
        }
    }

    private List<String> getNames(Collection<PluginMeta> metas) {
        return metas.stream().map(PluginMeta::name).collect(Collectors.toList());
    }

    private List<String> getHardDependencies(PluginMeta meta) {
        return Arrays.stream(meta.dependencies()).filter(dependency -> dependency.type() == DependencyType.HARD).map(Dependency::name).collect(Collectors.toList());
    }

    private List<String> getSoftDependencies(Collection<PluginMeta> metas, PluginMeta meta) {
        List<String> names  = getNames(metas);
        return Arrays.stream(meta.dependencies())
                .filter(dependency -> dependency.type() == DependencyType.SOFT).filter(dependency -> names.contains(dependency.name()))
                .map(Dependency::name).collect(Collectors.toList());
    }

    public void disablePlugins() {
        if (plugins.isEmpty()) return;
        Conduit.getLogger().info("Disabling plugins...");
        // Loop through all plugins and disable them
        Iterator<Plugin> iterator = plugins.iterator();
        while (iterator.hasNext()) {
            Plugin plugin = iterator.next();
            // Disable the systems.conduit.core.plugin
            disable(plugin, false);
            // Remove systems.conduit.core.plugin from list
            iterator.remove();
        }
    }

    public void enable(Plugin plugin, boolean reload) {
        // Enable now
        if (!reload) Conduit.getLogger().info("Enabling systems.conduit.core.plugin: " + plugin.getMeta().name());
        plugin.setPluginState(PluginState.ENABLING);
        plugin.onEnable();
        plugin.setPluginState(PluginState.ENABLED);
        if (!reload) Conduit.getLogger().info("Enabled systems.conduit.core.plugin: " + plugin.getMeta().name());
    }

    public void disable(Plugin plugin, boolean server) {
        if ((plugin.getPluginState() == PluginState.DISABLING || plugin.getPluginState() == PluginState.DISABLED)) return;
        if (!server) Conduit.getLogger().info("Disabling systems.conduit.core.plugin: " + plugin.getMeta().name());
        plugin.setPluginState(PluginState.DISABLING);
        plugin.getEvents().clear();
        plugin.onDisable();
        plugin.setPluginState(PluginState.DISABLED);
        if (!server) Conduit.getLogger().info("Disabled systems.conduit.core.plugin: " + plugin.getMeta().name());
    }

    public void reloadPlugins(boolean server, Callback callback) {
        if (!server) Conduit.getLogger().info("Reloading all plugins...");
        plugins.forEach(plugin -> reload(plugin, false));
        callback.result(new Object[]{});
        if (!server) Conduit.getLogger().info("Reloaded all plugins");
    }

    public void reload(Plugin plugin, boolean server) {
        // Before we attempt to reload the systems.conduit.core.plugin, make sure that it can safely be done.
        if (!plugin.getMeta().reloadable()) {
            // This systems.conduit.core.plugin is not reloadable.
            return;
        }
        if (!server) Conduit.getLogger().info("Reloading systems.conduit.core.plugin: " + plugin.getMeta().name());
        // Unload the systems.conduit.core.plugin
        disable(plugin, true);
        AtomicReference<Optional<File>> pluginFile = new AtomicReference<>(Optional.empty());
        // Remove the systems.conduit.core.plugin if found and store the file for later
        plugins.removeIf(entryPlugin -> {
            pluginFile.set(Optional.ofNullable(entryPlugin.getClassLoader().getPluginFile()));
            return entryPlugin.equals(plugin);
        });
        // Load systems.conduit.core.plugin again
        pluginFile.get().ifPresent(file -> loadPlugins(Collections.singletonList(file), true));
        if (!server) Conduit.getLogger().info("Reloaded systems.conduit.core.plugin: " + plugin.getMeta().name());

        ServerEvents.PluginReloadEvent event = new ServerEvents.PluginReloadEvent(plugin);
        Conduit.getEventManager().dispatchEvent(event);
    }

    public Optional<Plugin> getPlugin(String name) {
        return plugins.parallelStream().filter(plugin -> plugin.getMeta().name().equalsIgnoreCase(name)).findFirst();
    }
}
