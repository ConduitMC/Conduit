package systems.conduit.core.plugin

import net.minecraft.ChatFormatting
import net.minecraft.network.chat.TextComponent
import systems.conduit.core.Conduit
import systems.conduit.core.commands.BaseCommand
import systems.conduit.core.datastore.DatastoreController
import systems.conduit.core.events.EventListener
import systems.conduit.core.plugin.annotation.PluginMeta
import systems.conduit.core.plugin.config.Configuration
import java.lang.reflect.Method
import java.util.concurrent.ConcurrentHashMap

// TODO: This needs to be moved to the API
abstract class Plugin {

    internal lateinit var classLoader: PluginClassLoader

    lateinit var meta: PluginMeta

    var pluginState: PluginState = PluginState.UNLOADED
        internal set

    var events: MutableMap<Int, MutableMap<EventListener, MutableList<Method>>> = ConcurrentHashMap<Int, MutableMap<EventListener, MutableList<Method>>>()
        private set

    var config: Configuration? = null

    var datastore: DatastoreController? = null

    abstract fun onEnable()
    abstract fun onDisable()

    @SafeVarargs
    protected fun registerListeners(vararg clazz: Class<out EventListener>) = clazz.forEach { Conduit.eventManager.registerEventClass(this, it) }

    protected fun registerCommands(vararg commands: BaseCommand?) {
        Conduit.commandManager.registerCommand(*commands)
    }

    fun toStringColored(): String {
        var color: ChatFormatting = ChatFormatting.RED
        if (pluginState == PluginState.ENABLING || pluginState == PluginState.DISABLING) color = ChatFormatting.YELLOW
        if (pluginState == PluginState.ENABLED) color = ChatFormatting.GREEN
        return TextComponent(meta.name).withStyle(color).coloredString
    }

    override fun toString(): String {
        return meta.name
    }
}