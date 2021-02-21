package systems.conduit.main.core.plugin.config.loader;

import com.moandjiezana.toml.Toml;
import systems.conduit.main.core.plugin.config.Configuration;
import systems.conduit.main.core.plugin.config.ConfigurationLoader;

import java.io.File;
import java.util.Optional;

/**
 * @author Innectic
 * @since 12/31/2020
 */
public class TomlLoader implements ConfigurationLoader {

    @Override
    public Optional<Configuration> load(File file, Class<? extends Configuration> configuration) {
        try {
            Toml toml = new Toml().read(file);
            return Optional.of(toml.to(configuration));
        } catch (Exception ignored) {}
        return Optional.empty();
    }
}
