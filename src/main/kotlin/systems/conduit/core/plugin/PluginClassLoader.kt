package systems.conduit.core.plugin

import org.reflections.Reflections
import org.reflections.util.ConfigurationBuilder
import systems.conduit.core.Conduit
import systems.conduit.core.datastore.DatastoreController
import systems.conduit.core.plugin.annotation.PluginMeta
import systems.conduit.core.plugin.config.Configuration
import systems.conduit.core.plugin.config.ConfigurationTypes
import systems.conduit.core.plugin.config.annotation.ConfigFile
import systems.conduit.core.plugin.config.defaults.DefaultConfigurationHandler
import java.io.File
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Paths
import java.security.CodeSource
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.jar.JarFile
import java.util.stream.Collectors
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

class PluginClassLoader internal constructor(var pluginFile: File, parent: ClassLoader?): URLClassLoader(arrayOf(pluginFile.toURI().toURL()), parent) {

    private val classesCache: MutableMap<String, Class<*>?> = ConcurrentHashMap()

    private val url = pluginFile.toURI().toURL()
    private val jar = JarFile(pluginFile)

    fun loadMeta(): PluginMeta? {
        val reflections = Reflections(ConfigurationBuilder().setUrls(url).addClassLoader(this))
        val annotated = reflections.getSubTypesOf(Plugin::class.java).stream().filter { c: Class<out Plugin> -> c.isAnnotationPresent(PluginMeta::class.java) }.collect(Collectors.toSet())
        // Since we have the plugin class, we'll first grab the annotation and make sure that all the values are present.
        for (pluginClass in annotated) {
            // Get the plugin meta
            val meta = pluginClass.getAnnotation(PluginMeta::class.java)
            if (meta == null) {
                Conduit.logger.error("INTERNAL ERROR: failed to load plugin class: " + pluginClass.name + ": cannot find annotation that previously existed.")
                return null
            }
            return meta
        }
        return null
    }

    fun load(meta: PluginMeta): Plugin? {
        val reflections = Reflections(ConfigurationBuilder().setUrls(url).addClassLoader(this))
        val annotated = reflections.getSubTypesOf(Plugin::class.java).stream().filter { c: Class<out Plugin> -> c.isAnnotationPresent(PluginMeta::class.java) }.collect(Collectors.toSet())
        // Since we have the plugin class, we'll first grab the annotation and make sure that all the values are present.
        for (pluginClass in annotated) {
            // Now that we have our meta information, we can attempt to create an instance of this plugin.
            var plugin: Plugin? = null
            try {
                val constructor = pluginClass.getConstructor()
                plugin = constructor.newInstance()
            } catch (e: NoSuchMethodException) {
                Conduit.logger.error("INTERNAL ERROR: failed to create instance of plugin: " + meta.name)
                e.printStackTrace()
            } catch (e: InstantiationException) {
                Conduit.logger.error("INTERNAL ERROR: failed to create instance of plugin: " + meta.name)
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                Conduit.logger.error("INTERNAL ERROR: failed to create instance of plugin: " + meta.name)
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                Conduit.logger.error("INTERNAL ERROR: failed to create instance of plugin: " + meta.name)
                e.printStackTrace()
            }
            // Double check that we have an instance of the plugin
            if (plugin == null) {
                Conduit.logger.error("INTERNAL ERROR: empty plugin instance leaked past try for " + meta.name)
                return null
            }
            plugin.classLoader = this
            plugin.datastore = DatastoreController(meta.name)
            plugin.meta = meta
            // Now, we can try to get the config for this plugin.
            val clazz: KClass<out Configuration> = meta.config
            val config = loadConfiguration(plugin, clazz.java)
            if (config != null) plugin.config = config
            return plugin
        }
        return null
    }

    private fun loadConfiguration(plugin: Plugin, clazz: Class<out Configuration>): Configuration? {
        if (!clazz.javaClass.isAnnotationPresent(ConfigFile::class.java)) return null
        val annotation = clazz.getAnnotation(ConfigFile::class.java)
        val path = Paths.get("plugins", plugin.meta.name).toAbsolutePath()
        // Ensure that the path exists. If it doesn't, then we don't need to process this.
        val pluginFolder = path.toFile()
        if (!pluginFolder.exists()) {
            if (!pluginFolder.mkdirs()) Conduit.logger.error("Failed to make plugin directory")
            return null
        }
        val file: File = path.resolve(annotation.name + "." + annotation.type).toFile()
        if (!file.exists()) {
            // If the file does not exist, then lets attempt to generate a default.
            DefaultConfigurationHandler.handleDefaultForPlugin(file.absolutePath, plugin)
            try {
                // TODO: Implement configuration defaults
                if (!file.createNewFile()) Conduit.logger.error("Failed to create configuration file")
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return ConfigurationTypes.getLoaderForExtension(annotation.type)?.load(file, clazz)
    }

    @Throws(ClassNotFoundException::class)
    override fun findClass(name: String): Class<*>? {
        var result = classesCache[name]
        if (result == null) {
            val path = name.replace('.', '/') + ".class"
            val entry = jar.getJarEntry(path)
            if (entry != null) {
                var classBytes: ByteArray = byteArrayOf()
                try {
                    jar.getInputStream(entry).use { `is` ->
                        val targetArray = ByteArray(`is`.available())
                        `is`.read(targetArray)
                        classBytes = targetArray
                    }
                } catch (ex: IOException) {
                    throw ClassNotFoundException(name, ex)
                }
                val signers = entry.codeSigners
                val source = CodeSource(url, signers)
                result = defineClass(name, classBytes, 0, classBytes.size, source)
            }
            if (result == null) {
                result = super.findClass(name)
            }
            classesCache[name] = result
        }
        return result
    }
}
