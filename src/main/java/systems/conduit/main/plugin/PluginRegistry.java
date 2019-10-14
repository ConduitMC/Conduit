package systems.conduit.main.plugin;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PluginRegistry {

    @Getter(AccessLevel.MODULE) private Map<Plugin, PluginClassLoader> plugins = new HashMap<>();

    void registerPlugin(Plugin plugin, PluginClassLoader classLoader) {
        plugins.put(plugin, classLoader);
    }

    public Optional<Plugin> getPlugin(String name) {
        return plugins.keySet().parallelStream().filter(plugin -> plugin.getMeta().name().equalsIgnoreCase(name)).findFirst();
    }
}
