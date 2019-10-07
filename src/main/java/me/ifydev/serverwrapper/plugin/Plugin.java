package me.ifydev.serverwrapper.plugin;

import lombok.Getter;

/**
 * @author Innectic
 * @since 10/6/2019
 */
@Getter
public abstract class Plugin {

    private PluginState state = PluginState.UNLOADED;

    protected abstract void onEnable();
    protected abstract void onDisable();
}
