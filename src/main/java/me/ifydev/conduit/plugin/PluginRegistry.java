package me.ifydev.conduit.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Innectic
 * @since 10/7/2019
 */
public class PluginRegistry {

    private Map<String, Plugin> plugins = new HashMap<>();

    public void registerPlugin(Plugin plugin) {
        plugins.put(plugin.getMeta().name(), plugin);
    }

    public Optional<Plugin> getPlugin(String name) {
        return Optional.ofNullable(plugins.getOrDefault(name, null));
    }
}
