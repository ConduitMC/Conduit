package me.ifydev.conduit.plugin;

import me.ifydev.conduit.Conduit;
import me.ifydev.conduit.plugin.annotation.PluginMeta;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Innectic
 * @since 10/6/2019
 */
public class PluginLoader {

    private static Reflections reflections = new Reflections();

    /**
     * Finds all classes in runtime that extend {@link Plugin} and are annotated with {@link me.ifydev.conduit.plugin.annotation.PluginMeta}
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
}
