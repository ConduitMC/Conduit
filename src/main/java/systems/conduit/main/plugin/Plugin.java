package systems.conduit.main.plugin;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextComponent;
import systems.conduit.main.Conduit;
import systems.conduit.main.events.EventListener;
import systems.conduit.main.plugin.annotation.PluginMeta;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Plugin {

    @Getter(AccessLevel.MODULE) @Setter(AccessLevel.MODULE) PluginClassLoader classLoader;
    @Getter @Setter(AccessLevel.MODULE) private PluginMeta meta;
    @Getter @Setter(AccessLevel.MODULE) private PluginState pluginState = PluginState.UNLOADED;
    @Getter(AccessLevel.PUBLIC) private Map<Integer, Map<EventListener, List<Method>>> events = new ConcurrentHashMap<>();

    protected abstract void onEnable();
    protected abstract void onDisable();

    @SafeVarargs
    protected final void registerListeners(Class<? extends EventListener>... clazz) {
        Arrays.stream(clazz).forEach(aClass -> Conduit.getEventManager().registerEventClass(this, aClass));
    }

    public String toStringColored() {
        ChatFormatting color = ChatFormatting.RED;
        if (pluginState == PluginState.ENABLING || pluginState == PluginState.DISABLING) color = ChatFormatting.YELLOW;
        if (pluginState == PluginState.ENABLED) color = ChatFormatting.GREEN;
        return new TextComponent(meta.name()).withStyle(color).getColoredString();
    }

    @Override
    public String toString() {
        return meta.name();
    }
}
