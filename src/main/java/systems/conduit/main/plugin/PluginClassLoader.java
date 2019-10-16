package systems.conduit.main.plugin;

import lombok.Getter;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import systems.conduit.main.Conduit;
import systems.conduit.main.plugin.annotation.PluginMeta;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PluginClassLoader extends URLClassLoader {

    @Getter private final File pluginFile;

    PluginClassLoader(File pluginFile, ClassLoader parent) throws MalformedURLException {
        super(new URL[] {pluginFile.toURI().toURL()}, parent);
        this.pluginFile = pluginFile;
    }

    Optional<Plugin> load() {
        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(getURLs()).addClassLoader(this));
        Set<Class<? extends Plugin>> annotated = reflections.getSubTypesOf(Plugin.class).stream().filter(c -> c.isAnnotationPresent(PluginMeta.class)).collect(Collectors.toSet());
        // TODO: Load plugins in order, based on the dependencies listed in the annotation.
        // Since we have the plugin class, we'll first grab the annotation and make sure that all the values are present.
        for (Class<? extends Plugin> pluginClass : annotated) {
            // Get the plugin meta
            PluginMeta meta = pluginClass.getAnnotation(PluginMeta.class);
            if (meta == null) {
                Conduit.LOGGER.error("INTERNAL ERROR: failed to load plugin class: " + pluginClass.getName() + ": cannot find annotation that previously existed.");
                return Optional.empty();
            }
            // Now that we have our meta information, we can attempt to create an instance of this plugin.
            Optional<Plugin> plugin = Optional.empty();
            try {
                Constructor<? extends Plugin> constructor = pluginClass.getConstructor();
                plugin = Optional.of(constructor.newInstance());
            } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                Conduit.LOGGER.error("INTERNAL ERROR: failed to create instance of plugin: " + meta.name());
                e.printStackTrace();
            }
            // Double check that we have an instance of the plugin
            if (!plugin.isPresent()) {
                Conduit.LOGGER.error("INTERNAL ERROR: empty plugin instance leaked past try for " + meta.name());
                return Optional.empty();
            }
            plugin.get().setClassLoader(this);
            plugin.get().setMeta(meta);
            return plugin;
        }
        return Optional.empty();
    }
}
