package systems.conduit.main.plugin;

import lombok.AccessLevel;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PluginRegistry {

    @Getter(AccessLevel.MODULE) private Map<String, Plugin> plugins = new HashMap<>();

    public void registerPlugin(Plugin plugin) {
        plugins.put(plugin.getMeta().name(), plugin);
    }

    public Optional<Plugin> getPlugin(String name) {
        return Optional.ofNullable(plugins.getOrDefault(name, null));
    }
}
