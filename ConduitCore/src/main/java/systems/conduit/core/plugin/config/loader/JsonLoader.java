package systems.conduit.core.plugin.config.loader;

import com.google.gson.Gson;
import systems.conduit.core.Conduit;
import systems.conduit.core.plugin.config.Configuration;
import systems.conduit.core.plugin.config.ConfigurationLoader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

/*
 * @author Innectic
 * @since 10/16/2019
 */
public class JsonLoader implements ConfigurationLoader {

    private static final Gson gson = new Gson();

    @Override
    public Optional<Configuration> load(File file, Class<? extends Configuration> configurationType) {
        try {
            Configuration value = gson.fromJson(new FileReader(file), configurationType);
            return Optional.of(value);
        } catch (IOException e) {
            Conduit.getLogger().error("Failed to load configuration file: " + file);
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
