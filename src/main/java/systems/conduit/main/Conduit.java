package systems.conduit.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import systems.conduit.main.events.EventManager;
import systems.conduit.main.plugin.PluginManager;

public class Conduit {

    public final static Logger LOGGER = LogManager.getLogger("Conduit");
    public static EventManager eventManager = new EventManager();
    public static PluginManager pluginManager = new PluginManager();

}
