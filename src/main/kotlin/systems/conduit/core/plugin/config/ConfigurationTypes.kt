package systems.conduit.core.plugin.config

import systems.conduit.core.Conduit
import systems.conduit.core.plugin.config.loader.JsonLoader
import java.lang.reflect.InvocationTargetException
import java.util.*

/*
 * @author Innectic
 * @since 10/17/2019
 */
object ConfigurationTypes {

    private val loaderCache: MutableMap<String, ConfigurationLoader> = HashMap()
    private val loaderTypes: MutableMap<String, Class<out ConfigurationLoader>?> = HashMap()

    fun getLoaderForExtension(extension: String): ConfigurationLoader? {
        // First, check if we currently have an already created loader cached.
        if (loaderCache.containsKey(extension)) return loaderCache[extension]!!

        // Since it does not currently contain an instance, lets attempt to make one.
        val loaderClass = loaderTypes.getOrDefault(extension, null) ?: return null
        // Invalid extension provided.

        // Valid extension, so we have a class type and can make a new cache loader.
        try {
            val loader = loaderClass.getConstructor().newInstance()
            loaderCache[extension] = loader
            return loader
        } catch (e: InstantiationException) {
            Conduit.logger.error("Failed to create instance of configuration loader for $extension:")
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            Conduit.logger.error("Failed to create instance of configuration loader for $extension:")
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            Conduit.logger.error("Failed to create instance of configuration loader for $extension:")
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            Conduit.logger.error("Failed to create instance of configuration loader for $extension:")
            e.printStackTrace()
        }
        return null
    }

    fun drop(extension: String) {
        loaderCache.remove(extension)
    }

    init {
        loaderTypes["json"] = JsonLoader::class.java
    }
}