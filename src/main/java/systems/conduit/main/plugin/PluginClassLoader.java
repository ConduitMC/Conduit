package systems.conduit.main.plugin;

import lombok.Getter;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import systems.conduit.main.Conduit;
import systems.conduit.main.plugin.annotation.PluginMeta;
import systems.conduit.main.plugin.config.annotation.ConfigFile;
import systems.conduit.main.plugin.config.Configuration;
import systems.conduit.main.plugin.config.ConfigurationLoader;
import systems.conduit.main.plugin.config.ConfigurationTypes;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

public class PluginClassLoader extends URLClassLoader {

    private final Map<String, Class<?>> classesCache = new ConcurrentHashMap<>();
    @Getter private final File pluginFile;
    private final JarFile jar;
    private final URL url;

    PluginClassLoader(File pluginFile, ClassLoader parent) throws IOException {
        super(new URL[]{pluginFile.toURI().toURL()}, parent);
        this.pluginFile = pluginFile;
        this.jar = new JarFile(pluginFile);
        this.url = pluginFile.toURI().toURL();
    }

    Optional<PluginMeta> loadMeta() {
        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(url).addClassLoader(this));
        Set<Class<? extends Plugin>> annotated = reflections.getSubTypesOf(Plugin.class).stream().filter(c -> c.isAnnotationPresent(PluginMeta.class)).collect(Collectors.toSet());
        // TODO: Load plugins in order, based on the dependencies listed in the annotation.
        // Since we have the plugin class, we'll first grab the annotation and make sure that all the values are present.
        for (Class<? extends Plugin> pluginClass : annotated) {
            // Get the plugin meta
            PluginMeta meta = pluginClass.getAnnotation(PluginMeta.class);
            if (meta == null) {
                Conduit.getLogger().error("INTERNAL ERROR: failed to load plugin class: " + pluginClass.getName() + ": cannot find annotation that previously existed.");
                return Optional.empty();
            }
            return Optional.of(meta);
        }
        return Optional.empty();
    }

    Optional<Plugin> load(PluginMeta meta) {
        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(url).addClassLoader(this));
        Set<Class<? extends Plugin>> annotated = reflections.getSubTypesOf(Plugin.class).stream().filter(c -> c.isAnnotationPresent(PluginMeta.class)).collect(Collectors.toSet());
        // TODO: Load plugins in order, based on the dependencies listed in the annotation.
        // Since we have the plugin class, we'll first grab the annotation and make sure that all the values are present.
        for (Class<? extends Plugin> pluginClass : annotated) {
            // Now that we have our meta information, we can attempt to create an instance of this plugin.
            Optional<Plugin> plugin = Optional.empty();
            try {
                Constructor<? extends Plugin> constructor = pluginClass.getConstructor();
                plugin = Optional.of(constructor.newInstance());
            } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
                Conduit.getLogger().error("INTERNAL ERROR: failed to create instance of plugin: " + meta.name());
                e.printStackTrace();
            }
            // Double check that we have an instance of the plugin
            if (!plugin.isPresent()) {
                Conduit.getLogger().error("INTERNAL ERROR: empty plugin instance leaked past try for " + meta.name());
                return Optional.empty();
            }
            plugin.get().setClassLoader(this);
            plugin.get().setMeta(meta);
            // Now, we can try to get the config for this plugin.
            Class<? extends Configuration> clazz = meta.config();
            loadConfiguration(meta.name(), clazz).ifPresent(plugin.get()::setConfig);
            return plugin;
        }
        return Optional.empty();
    }

    private Optional<Configuration> loadConfiguration(String plugin, Class<? extends Configuration> clazz) {
        if (!clazz.isAnnotationPresent(ConfigFile.class)) return Optional.empty();
        ConfigFile annotation = clazz.getAnnotation(ConfigFile.class);
        Path path = Paths.get("plugins", plugin).toAbsolutePath();
        // Ensure that the path exists. If it doesn't, then we don't need to process this.
        File pluginFolder = path.toFile();
        if (!pluginFolder.exists()) {
            if (!pluginFolder.mkdirs()) Conduit.getLogger().error("Failed to make plugin directory");
            return Optional.empty();
        }
        File file = path.resolve(annotation.name() + "." + annotation.type()).toFile();
        if (!file.exists()) {
            try {
                // TODO: Implement configuration defaults
                if (!file.createNewFile()) Conduit.getLogger().error("Failed to create configuration file");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Optional<ConfigurationLoader> loader = ConfigurationTypes.getLoaderForExtension(annotation.type());
        if (!loader.isPresent()) return Optional.empty();
        return loader.get().load(file, clazz);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Class<?> result = classesCache.get(name);
        if (result == null) {
            String path = name.replace('.', '/').concat(".class");
            JarEntry entry = jar.getJarEntry(path);
            if (entry != null) {
                byte[] classBytes;
                try (InputStream is = jar.getInputStream(entry)) {
                    byte[] targetArray = new byte[is.available()];
                    is.read(targetArray);
                    classBytes = targetArray;
                } catch (IOException ex) {
                    throw new ClassNotFoundException(name, ex);
                }
                CodeSigner[] signers = entry.getCodeSigners();
                CodeSource source = new CodeSource(url, signers);
                result = defineClass(name, classBytes, 0, classBytes.length, source);
            }
            if (result == null) {
                result = super.findClass(name);
            }
            classesCache.put(name, result);
        }
        return result;
    }
}
