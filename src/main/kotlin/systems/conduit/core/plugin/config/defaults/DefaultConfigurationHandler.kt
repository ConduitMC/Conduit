package systems.conduit.core.plugin.config.defaults

import com.google.gson.Gson
import com.google.gson.JsonIOException
import systems.conduit.core.Conduit
import systems.conduit.core.plugin.Plugin
import systems.conduit.core.plugin.config.Configuration
import systems.conduit.core.plugin.config.annotation.ConfigFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

/*
 * @author Innectic
 * @since 10/26/2019
 */
object DefaultConfigurationHandler {

    fun handleDefaultForPlugin(destination: String, plugin: Plugin) {
        if (!plugin.getConfig<Configuration>().isPresent) {
            // The systems.conduit.core.plugin does not have a configuration, so we don't care.
            return
        }

        // Make sure this looks like a real config.
        if (!plugin.getConfig<Configuration>().get().javaClass.isAnnotationPresent(ConfigFile::class.java)) {
            // The systems.conduit.core.plugin class does not have the ConfigFile annotation, so this is not a real config.
            return
        }

        // Since this systems.conduit.core.plugin has a configuration present, next we need to get the config annotation. Then, we need to check if it has a default
        // file listed on it. If it does, then we want to copy the file. If it does not have a default file given, then we want to create
        // our own default entries.
        val configFileAnnotation = plugin.getConfig<Configuration>().get().javaClass.getAnnotation(ConfigFile::class.java)
        if (!configFileAnnotation.defaultFile().equals("", ignoreCase = true)) {
            // A default configuration file was given, so lets copy that.
            copyDefaultConfiguration(configFileAnnotation.defaultFile(), destination, plugin)
            return
        }
        // Looks like a default file was not given, so we're going to attempt to generate our own defaults.
        generateDefault(destination, plugin)
    }

    private fun copyDefaultConfiguration(defaultFile: String, destination: String, plugin: Plugin) {
        val source = Paths.get(plugin.javaClass.getResource(defaultFile).path)
        val dest = Paths.get(destination)
        if (!source.toFile().exists()) {
            // The source file does not exist, so we can't copy it.
            Conduit.logger.error(plugin.meta.name() + " does not have a default configuration file provided.")
            return
        }
        try {
            Files.copy(source, dest)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun generateDefault(destination: String, plugin: Plugin) {
        if (!plugin.getConfig<Configuration>().isPresent) return
        val defaultConfigOptions = DefaultParser.generateDefaults(plugin.getConfig<Configuration>().get())
        try {
            val destinationFile = Files.createFile(Paths.get(destination))
            val defaultString = Gson().toJson(defaultConfigOptions)
            Files.write(destinationFile, defaultString.toByteArray())
        } catch (e: JsonIOException) {
            Conduit.logger.error("Failed to generate new default config")
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}