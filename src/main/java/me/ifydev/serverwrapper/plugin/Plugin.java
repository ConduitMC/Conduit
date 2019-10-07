package me.ifydev.serverwrapper.plugin;

import lombok.Getter;
import me.ifydev.serverwrapper.plugin.annotation.PluginMeta;

import java.util.Optional;

/**
 * @author Innectic
 * @since 10/6/2019
 */
public abstract class Plugin {

    @Getter private PluginState state = PluginState.UNLOADED;
    private Optional<PluginMeta> meta;

    protected abstract void onEnable();
    protected abstract void onDisable();
}
