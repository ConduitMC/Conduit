package systems.conduit.main.plugin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import systems.conduit.main.plugin.annotation.PluginMeta;

/**
 * @author Innectic
 * @since 10/6/2019
 */
@RequiredArgsConstructor
public abstract class Plugin {

    @Getter private final PluginMeta meta;
    @Getter @Setter private PluginState pluginState = PluginState.UNLOADED;

    protected abstract void onEnable();
    protected abstract void onDisable();
}
