package systems.conduit.main.plugin.config.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import systems.conduit.main.Conduit;
import systems.conduit.main.plugin.config.Configuration;
import systems.conduit.main.plugin.config.ConfigurationLoader;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/*
 * @author Innectic
 * @since 10/16/2019
 */
public class JsonLoader implements ConfigurationLoader {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Optional<Configuration> load(File file, Class<? extends Configuration> configurationType) {
        try {
            Configuration value = mapper.readValue(file, configurationType);
            return Optional.of(value);
        } catch (IOException e) {
            Conduit.getLogger().error("Failed to load configuration file: " + file);
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
