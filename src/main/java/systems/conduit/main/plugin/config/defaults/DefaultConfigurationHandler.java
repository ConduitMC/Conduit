package systems.conduit.main.plugin.config.defaults;

import com.fasterxml.jackson.core.JsonProcessingException;
import systems.conduit.main.Conduit;
import systems.conduit.main.plugin.Plugin;
import systems.conduit.main.plugin.config.annotation.ConfigFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author Innectic
 * @since 10/26/2019
 */
public class DefaultConfigurationHandler {

    public static void handleDefaultForPlugin(String destination, Plugin plugin) {
        if (!plugin.getConfig().isPresent()) {
            // The plugin does not have a configuration, so we don't care.
            return;
        }

        // Make sure this looks like a real config.
        if (!plugin.getConfig().get().getClass().isAnnotationPresent(ConfigFile.class)) {
            // The plugin class does not have the ConfigFile annotation, so this is not a real config.
            return;
        }

        // Since this plugin has a configuration present, next we need to get the config annotation. Then, we need to check if it has a default
        // file listed on it. If it does, then we want to copy the file. If it does not have a default file given, then we want to create
        // our own default entries.
        ConfigFile configFileAnnotation = plugin.getConfig().get().getClass().getAnnotation(ConfigFile.class);
        if (!configFileAnnotation.defaultFile().equalsIgnoreCase("")) {
            // A default configuration file was given, so lets copy that.
            copyDefaultConfiguration(configFileAnnotation.defaultFile(), destination, plugin);
            return;
        }
        // Looks like a default file was not given, so we're going to attempt to generate our own defaults.
        generateDefault(destination, plugin);
    }

    private static void copyDefaultConfiguration(String defaultFile, String destination, Plugin plugin) {
        Path source = Paths.get(plugin.getClass().getResource(defaultFile).getPath());
        Path dest = Paths.get(destination);
        if (!source.toFile().exists()) {
            // The source file does not exist, so we can't copy it.
            Conduit.getLogger().error(plugin.getMeta().name() + " does not have a default configuration file provided.");
            return;
        }

        try {
            Files.copy(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateDefault(String destination, Plugin plugin) {
        if (!plugin.getConfig().isPresent()) return;

        Map<String, Object> defaultConfigOptions = DefaultParser.generateDefaults(plugin.getConfig().get());
        try {
            Path destinationFile = Files.createFile(Paths.get(destination));
            String defaultString = Conduit.getObjectMapper().writeValueAsString(defaultConfigOptions);
            Files.write(destinationFile, defaultString.getBytes());
        } catch (JsonProcessingException e) {
            Conduit.getLogger().error("Failed to generate new default config");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
