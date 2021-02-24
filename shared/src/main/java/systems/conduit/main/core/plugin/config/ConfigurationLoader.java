package systems.conduit.main.core.plugin.config;

import java.io.File;
import java.util.Optional;

/*
 * @author Innectic
 * @since 10/16/2019
 */
public interface ConfigurationLoader {

    Optional<Configuration> load(File file, Class<? extends Configuration> configurationType);
}
