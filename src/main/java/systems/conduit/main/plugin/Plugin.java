package systems.conduit.main.plugin;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import systems.conduit.main.Conduit;
import systems.conduit.main.events.EventListener;
import systems.conduit.main.plugin.annotation.PluginMeta;

import java.util.Arrays;

public abstract class Plugin {

    @Getter @Setter(AccessLevel.MODULE) private PluginMeta meta;
    @Getter @Setter(AccessLevel.MODULE) private PluginState pluginState = PluginState.UNLOADED;

    protected abstract void onEnable();
    protected abstract void onDisable();

    // TODO: Pass plugin here so we can keep track of it all?
    @SafeVarargs
    protected final void registerListeners(Class<? extends EventListener>... clazz) {
        Arrays.stream(clazz).forEach(Conduit.eventManager::registerEventClass);
    }
}
