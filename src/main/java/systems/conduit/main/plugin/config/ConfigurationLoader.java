package systems.conduit.main.plugin.config;

import java.util.Optional;

/**
 * @author Innectic
 * @since 10/16/2019
 */
public interface ConfigurationLoader {

    <T extends Configuration> Optional<T> load(String file, Class<T> configurationType);
}
