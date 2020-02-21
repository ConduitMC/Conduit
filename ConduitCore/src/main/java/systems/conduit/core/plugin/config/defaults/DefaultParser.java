package systems.conduit.core.plugin.config.defaults;

import com.google.gson.annotations.SerializedName;
import systems.conduit.core.Conduit;
import systems.conduit.core.plugin.config.Configuration;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/*
 * @author Innectic
 * @since 10/26/2019
 */
public class DefaultParser {

    /**
     * Generate a default configuration from the given config.
     *
     * @param configuration the configuration file to create defaults for.
     * @return a default set of configuration options.
     */
    static Map<String, Object> generateDefaults(Configuration configuration) {
        Map<String, Object> defaults = new HashMap<>();

        Field[] fields = configuration.getClass().getDeclaredFields();
        for (Field field : fields) {
            // Check if this field should have a different name
            String name = field.isAnnotationPresent(SerializedName.class) ? field.getAnnotation(SerializedName.class).value() : field.getName();
            Object value = null;

            try {
                value = field.get(configuration);
            } catch (NullPointerException e) {
                // If it's an NPE, then this field did not have a default value on it. So, we're going to try and guess.
            } catch (IllegalAccessException e) {
                Conduit.getLogger().error("Failed to generate default configuration file.");
                e.printStackTrace();
            }

            if (value == null) {
                // If value is still null, then something funky happened. So, lets make sure the user knows.
                Conduit.getLogger().fatal("failed to find a default value for a field: " + name);
                continue;
            }

            defaults.put(name, value);
        }

        return defaults;
    }
}
