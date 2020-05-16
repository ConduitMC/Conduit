package systems.conduit.core.plugin

import com.google.common.collect.ArrayListMultimap
import com.google.common.collect.Multimap
import javassist.tools.Callback
import systems.conduit.core.Conduit
import systems.conduit.core.plugin.annotation.Dependency
import systems.conduit.core.plugin.annotation.DependencyType
import systems.conduit.core.plugin.annotation.PluginMeta
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicReference
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Collectors

class PluginManager {

    var plugins: Queue<Plugin> = ConcurrentLinkedQueue()
        private set

    /**
     * Finds all classes in runtime that extend [Plugin] and are annotated with [systems.conduit.core.plugin.annotation.PluginMeta]
     * and attempts to load it as a plugin.
     */
    fun loadPlugins() {
        Conduit.logger.info("Loading plugins...")
        // We can now get all the jars in the plugins folder and load all of them.
        val pluginsFolder = Paths.get("plugins").toFile()
        if (!pluginsFolder.exists() && !pluginsFolder.mkdirs()) {
            Conduit.logger.error("Failed to make plugins directory.")
            return
        }
        val pluginFiles = pluginsFolder.listFiles() ?: return
        val plugins: MutableList<File> = ArrayList()
        for (file in pluginFiles) {
            // Skip folders, and non-jars
            if (!file.isFile || !file.name.endsWith(".jar")) continue
            // Since it is a file, and it ends with .jar, we can proceed with attempting to load it.
            plugins.add(file)
        }
        loadPlugins(plugins, false)
    }

    private fun loadPlugins(pluginFiles: List<File>, reload: Boolean) {
        try {
            // List that should never be changed unless plugin is unable to ever be loaded.
            val metas: MutableList<PluginMeta> = ArrayList()
            // Loaders - Should be able to be removed from here if we loaded the plugin as plugin now has classloader on it.
            val loaders: MutableMap<PluginMeta?, PluginClassLoader> = HashMap()
            // Needed to be loaded first soft dependencies - Remove plugin from map and any lists
            val softDependencies: Multimap<PluginMeta?, String> = ArrayListMultimap.create()
            for (pluginFile in pluginFiles) {
                try {
                    PluginClassLoader(pluginFile, this.javaClass.classLoader).use { classLoader ->
                        val pluginMeta = classLoader.loadMeta()
                        if (pluginMeta != null) {
                            loaders[pluginMeta] = classLoader
                            metas.add(pluginMeta)
                        }
                    }
                } catch (e: IOException) {
                    Conduit.logger.error("Error loading plugin: " + pluginFile.name)
                    e.printStackTrace()
                }
            }
            for (meta in loaders.keys) {
                // All soft dependencies
                val otherDependencies = metas.stream().filter { m: PluginMeta? -> m != meta }.collect(Collectors.toList())
                softDependencies.putAll(meta, getSoftDependencies(otherDependencies, meta))
            }
            while (loaders.isNotEmpty()) {
                val it: MutableIterator<Map.Entry<PluginMeta?, PluginClassLoader>> = loaders.entries.iterator()
                while (it.hasNext()) {
                    val pl = it.next()
                    val hardDependencies = getHardDependencies(pl.key)
                    // Check to make sure all hard dependencies are able to be loaded
                    if (!getNames(metas).containsAll(getHardDependencies(pl.key))) {
                        Conduit.logger.error("Failed to load " + pl.key!!.name + " due to missing dependencies.")
                        hardDependencies.removeAll(getNames(metas))
                        Conduit.logger.error("Required dependencies: $hardDependencies")
                        // Can't load ever so remove all reference to plugin
                        softDependencies.asMap().remove(pl.key)
                        metas.remove(pl.key)
                        it.remove()
                        return
                    }
                    // Load soft soft dependencies first
                    if (softDependencies[pl.key].isEmpty()) {
                        val optionalPlugin = loaders[pl.key]!!.load(pl.key) ?: return
                        // The plugin should now be loaded, now we can attempt to enable the plugin.
                        enable(optionalPlugin, reload)
                        plugins.add(optionalPlugin)
                        softDependencies.values().remove(optionalPlugin.meta.name())
                        softDependencies.asMap().remove(optionalPlugin.meta)
                        it.remove()
                    }
                }
            }
        } catch (e: Exception) {
            Conduit.logger.error("Failed to load plugin.")
            e.printStackTrace()
        }
    }

    private fun getNames(metas: List<PluginMeta>): List<String?> {
        return metas.map { it.name }.toList()
    }

    private fun getHardDependencies(meta: PluginMeta) = meta.dependencies.filter { it.type == DependencyType.HARD }.map { it.name }
    private fun getSoftDependencies(metas: List<PluginMeta>, meta: PluginMeta) = meta.dependencies.filter { it.type == DependencyType.SOFT }.filter { getNames(metas).contains(it.name) }.map { it.name }

    fun disablePlugins() {
        if (plugins.isEmpty()) return
        Conduit.logger.info("Disabling plugins...")
        // Loop through all plugins and disable them
        val iterator = plugins.iterator()
        while (iterator.hasNext()) {
            val plugin = iterator.next()
            // Disable the plugin
            disable(plugin, false)
            // Remove plugin from list
            iterator.remove()
        }
    }

    fun enable(plugin: Plugin, reload: Boolean) {
        // Enable now
        if (!reload) Conduit.logger.info("Enabling plugin: " + plugin.meta.name())
        plugin.pluginState = PluginState.ENABLING
        plugin.onEnable()
        plugin.pluginState = PluginState.ENABLED
        if (!reload) Conduit.logger.info("Enabled plugin: " + plugin.meta.name())
    }

    fun disable(plugin: Plugin, server: Boolean) {
        if (plugin.pluginState == PluginState.DISABLING || plugin.pluginState == PluginState.DISABLED) return
        if (!server) Conduit.logger.info("Disabling plugin: " + plugin.meta.name())
        plugin.pluginState = PluginState.DISABLING
        plugin.events.clear()
        plugin.onDisable()
        plugin.pluginState = PluginState.DISABLED
        if (!server) Conduit.logger.info("Disabled plugin: " + plugin.meta.name())
    }

    fun reloadPlugins(server: Boolean, callback: Callback) {
        if (!server) Conduit.logger.info("Reloading all plugins...")
        plugins.forEach(Consumer { plugin: Plugin -> reload(plugin, false) })
        callback.result(arrayOf())
        if (!server) Conduit.logger.info("Reloaded all plugins")
    }

    fun reload(plugin: Plugin, server: Boolean) {
        // Before we attempt to reload the plugin, make sure that it can safely be done.
        if (!plugin.meta.reloadable()) {
            // This plugin is not reloadable.
            return
        }
        if (!server) Conduit.logger.info("Reloading plugin: " + plugin.meta.name())
        // Unload the plugin
        disable(plugin, true)
        val pluginFile = AtomicReference(Optional.empty<File>())
        // Remove the plugin if found and store the file for later
        plugins.removeIf { pl: Plugin ->
            pluginFile.set(Optional.ofNullable(pl.getClassLoader().pluginFile))
            pl == plugin
        }
        // Load plugin again
        pluginFile.get().ifPresent { file: File -> loadPlugins(listOf(file), true) }
        if (!server) Conduit.logger.info("Reloaded plugin: " + plugin.meta.name())
        val event = PluginReloadEvent(plugin)
        Conduit.eventManager.dispatchEvent(event)
    }

    fun getPlugin(name: String): Plugin? = plugins.first { it.meta.name().equals(name, ignoreCase = true) }

}
