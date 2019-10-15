package systems.conduit.main;

import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import systems.conduit.main.events.EventManager;
import systems.conduit.main.plugin.PluginLoader;
import systems.conduit.main.plugin.PluginRegistry;

import java.util.Optional;

public class Conduit {

    public final static Logger LOGGER = LogManager.getLogger("Conduit");
    public static EventManager eventManager = new EventManager();
    public static PluginLoader pluginLoader = new PluginLoader();
    public static PluginRegistry pluginRegistry = new PluginRegistry();

    public static Optional<MinecraftServer> server = Optional.empty();
}
