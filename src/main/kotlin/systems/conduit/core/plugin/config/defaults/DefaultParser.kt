package systems.conduit.core.plugin.config.defaults

import com.google.gson.annotations.SerializedName
import systems.conduit.core.Conduit
import systems.conduit.core.plugin.config.Configuration
import java.util.*

/*
 * @author Innectic
 * @since 10/26/2019
 */
object DefaultParser {
    /**
     * Generate a default configuration from the given config.
     *
     * @param configuration the configuration file to create defaults for.
     * @return a default set of configuration options.
     */
    fun generateDefaults(configuration: Configuration): Map<String, Any> {
        val defaults: MutableMap<String, Any> = HashMap()
        val fields = configuration.javaClass.declaredFields
        for (field in fields) {
            // Check if this field should have a different name
            val name = if (field.isAnnotationPresent(SerializedName::class.java)) field.getAnnotation(SerializedName::class.java).value else field.name
            var value: Any? = null
            try {
                value = field[configuration]
            } catch (e: NullPointerException) {
                // If it's an NPE, then this field did not have a default value on it. So, we're going to try and guess.
            } catch (e: IllegalAccessException) {
                Conduit.logger.error("Failed to generate default configuration file.")
                e.printStackTrace()
            }
            if (value == null) {
                // If value is still null, then something funky happened. So, lets make sure the user knows.
                Conduit.logger.fatal("failed to find a default value for a field: $name")
                continue
            }
            defaults[name] = value
        }
        return defaults
    }
}
