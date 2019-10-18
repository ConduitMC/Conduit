package systems.conduit.main.plugin.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Innectic
 * @since 10/16/2019
 */
@AllArgsConstructor
@Getter
public enum ConfigurationType {
    YAML("yml", null), JSON("json", null);

    private String extension;
    private Class<? extends ConfigurationLoader> loader;
}
