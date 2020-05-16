package systems.conduit.core.plugin

// TODO: This needs to be moved to the API
abstract class Plugin {
    @Getter(AccessLevel.MODULE)
    @Setter(AccessLevel.MODULE)
    var classLoader: PluginClassLoader? = null

    @Getter
    @Setter(AccessLevel.MODULE)
    val meta: PluginMeta? = null

    @Getter
    @Setter(AccessLevel.MODULE)
    private val pluginState: PluginState = PluginState.UNLOADED

    @Getter(AccessLevel.PUBLIC)
    val events: Map<Int, Map<EventListener, List<Method>>> = ConcurrentHashMap<Int, Map<EventListener, List<Method>>>()

    @Setter(AccessLevel.MODULE)
    private val config: Configuration? = null

    @Getter
    @Setter(AccessLevel.MODULE)
    private val datastore: DatastoreController? = null
    abstract fun onEnable()
    abstract fun onDisable()

    @SafeVarargs
    protected fun registerListeners(vararg clazz: Class<out EventListener?>?) {
        Arrays.stream(clazz).forEach(Consumer { eventClass: Class<out EventListener?>? -> Conduit.getEventManager().registerEventClass(this, eventClass) })
    }

    /**
     * Get the plugin's configuration in the type provided.
     *
     * @param <T> the configuration type
     * @return the plugin's configuration
    </T> */
    fun <T: Configuration?> getConfig(): Optional<T> {
        // TODO: Can this be improved?
        return if (config == null) Optional.empty() else try {
            Optional.of(config as T?)
        } catch (ignored: ClassCastException) {
            Optional.empty()
        }
    }

    protected fun registerCommands(vararg commands: BaseCommand?) {
        Conduit.getCommandManager().registerCommand(*commands)
    }

    fun toStringColored(): String {
        var color: ChatFormatting = ChatFormatting.RED
        if (pluginState == PluginState.ENABLING || pluginState == PluginState.DISABLING) color = ChatFormatting.YELLOW
        if (pluginState == PluginState.ENABLED) color = ChatFormatting.GREEN
        return TextComponent(meta.name()).withStyle(color).getColoredString()
    }

    override fun toString(): String {
        return meta.name()
    }
}