package systems.conduit.main.plugin;

import org.reflections.Reflections;
import systems.conduit.main.Conduit;
import systems.conduit.main.plugin.annotation.PluginMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PluginLoader {

    private static Reflections reflections = new Reflections();

    /**
     * Finds all classes in runtime that extend {@link Plugin} and are annotated with {@link systems.conduit.main.plugin.annotation.PluginMeta}
     * and attempts to load it as a plugin.
     */
    public void loadPlugins() {
        Set<Class<? extends Plugin>> annotated = reflections.getSubTypesOf(Plugin.class).stream()
                .filter(c -> c.isAnnotationPresent(PluginMeta.class)).collect(Collectors.toSet());

        for (Class<? extends Plugin> pluginClass : annotated) {
            // Since we have the plugin class, we'll first grab the annotation and make sure that all the values are present.
            // TODO: Load plugins in order, based on the dependencies listed in the annotation.
            PluginMeta meta = pluginClass.getAnnotation(PluginMeta.class);
            if (meta == null) {
                Conduit.LOGGER.error("INTERNAL ERROR: failed to load plugin class: " + pluginClass.getName() + ": cannot find annotation that previously existed.");
                return;
            }
            // Now that we have our meta information, we can attempt to create an instance of this plugin.
            Optional<Plugin> plugin = Optional.empty();
            try {
                Constructor<? extends Plugin> constructor = pluginClass.getConstructor(PluginMeta.class);
                plugin = Optional.of(constructor.newInstance(meta));
            } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                Conduit.LOGGER.error("INTERNAL ERROR: failed to create instance of plugin: " + meta.name());
                e.printStackTrace();
            }
            // Double check that we have an instance of the plugin
            if (!plugin.isPresent()) {
                Conduit.LOGGER.error("INTERNAL ERROR: empty plugin instance leaked past try for " + meta.name());
                return;
            }
            // We definitely have an instance of the plugin created. Now we can attempt to enable the plugin.
            plugin.get().setPluginState(PluginState.LOADING);
            plugin.get().onEnable();
            plugin.get().setPluginState(PluginState.LOADED);
            // The plugin is now loaded, put it into the registry.
            Conduit.pluginRegistry.registerPlugin(plugin.get());
            Conduit.LOGGER.info("Enabled plugin: " + meta.name());
        }
    }

    public void disablePlugins() {
        if (!Conduit.pluginRegistry.getPlugins().isEmpty()) Conduit.LOGGER.info("Disabling plugins...");
        // Loop through all plugins and disable them
        Iterator<Map.Entry<String, Plugin> > iterator = Conduit.pluginRegistry.getPlugins().entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Plugin> plugin = iterator.next();
            // Disable the plugin
            plugin.getValue().setPluginState(PluginState.DISABLING);
            plugin.getValue().onDisable();
            plugin.getValue().setPluginState(PluginState.DISABLED);
            // Remove plugin from list
            iterator.remove();
            Conduit.LOGGER.info("Disabled plugin: " + plugin.getKey());
        }
    }
}
