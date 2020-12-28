package systems.conduit.main.core.plugin;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.commands.BaseCommand;
import systems.conduit.main.core.datastore.Datastore;
import systems.conduit.main.core.datastore.DatastoreBackend;
import systems.conduit.main.core.datastore.schema.utils.DatastoreUtils;
import systems.conduit.main.core.events.EventListener;
import systems.conduit.main.core.plugin.annotation.PluginMeta;
import systems.conduit.main.core.plugin.config.Configuration;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Plugin {

    @Getter(AccessLevel.MODULE) @Setter(AccessLevel.MODULE) PluginClassLoader classLoader;
    @Getter @Setter(AccessLevel.MODULE) private PluginMeta meta;
    @Getter @Setter(AccessLevel.MODULE) private PluginState pluginState = PluginState.UNLOADED;
    @Getter(AccessLevel.PUBLIC) private final Map<Integer, Map<EventListener, List<Method>>> events = new ConcurrentHashMap<>();
    @Setter(AccessLevel.MODULE) private Configuration config = null;
    @Getter private final Map<DatastoreBackend, Datastore> datastores = new HashMap<>();

    protected abstract void onEnable();

    protected abstract void onDisable();

    protected final void registerListeners(EventListener... listeners) {
        Arrays.stream(listeners).forEach(listener -> Conduit.getEventManager().registerEventClass(this, listener));
    }

    /**
     * Get the plugin's configuration in the type provided.
     *
     * @param <T> the configuration type
     * @return the plugin's configuration
     */
    @SuppressWarnings("unchecked")
    public <T extends Configuration> Optional<T> getConfig() {
        // TODO: Can this be improved?

        if (this.config == null) return Optional.empty();
        try {
            return Optional.of((T) this.config);
        } catch (ClassCastException ignored) {
            return Optional.empty();
        }
    }

    public static <T extends Plugin> Optional<T> getPlugin(Class<T> type) {
        // Ensure the plugin meta annotation exists on the provided class
        if (!type.isAnnotationPresent(PluginMeta.class)) return Optional.empty();
        PluginMeta meta = type.getAnnotation(PluginMeta.class);

        return Conduit.getPluginManager().getPlugin(meta.name()).filter(p -> p.getClass().isAssignableFrom(type)).map(type::cast);
    }

    protected void registerCommands(BaseCommand... commands) {
        Conduit.getCommandManager().registerCommand(commands);
    }

    protected Optional<Datastore> getOrCreateDatastore(DatastoreBackend backend) {
        if (datastores.containsKey(backend)) return Optional.of(datastores.get(backend));

        // This plugin isn't already using this datastore backend. Create a new one and assign it here.
        Optional<Datastore> datastore = DatastoreUtils.createNewHandlerInstance(backend.getHandler());
        if (!datastore.isPresent()) {
            // Failed to create a new datastore!
            Conduit.getLogger().error("INTERNAL ERROR: Failed to initialize new datastore backend!");
            return Optional.empty();
        }

        Map<String, Object> meta = Conduit.getConfiguration().getDatastores().getMysql().toMeta();
        meta.put("database", getMeta().name());

        datastore.get().attach(meta);
        datastores.put(backend, datastore.get());

        return datastore;
    }

    public String toStringColored() {
        ChatFormatting color = ChatFormatting.RED;
        if (pluginState == PluginState.ENABLING || pluginState == PluginState.DISABLING) color = ChatFormatting.YELLOW;
        if (pluginState == PluginState.ENABLED) color = ChatFormatting.GREEN;
        return new TextComponent(meta.name()).withStyle(color).getContents();
    }

    @Override
    public String toString() {
        return meta.name();
    }
}
