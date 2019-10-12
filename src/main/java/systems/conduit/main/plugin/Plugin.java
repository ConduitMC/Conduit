package systems.conduit.main.plugin;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import systems.conduit.main.plugin.annotation.PluginMeta;

public abstract class Plugin {

    @Getter @Setter(AccessLevel.MODULE) private PluginMeta meta;
    @Getter @Setter(AccessLevel.MODULE) private PluginState pluginState = PluginState.UNLOADED;

    protected abstract void onEnable();
    protected abstract void onDisable();
}
