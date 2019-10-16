package systems.conduit.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import systems.conduit.main.commands.CommandManager;
import systems.conduit.main.events.EventManager;
import systems.conduit.main.plugin.PluginManager;

public class Conduit {

    private final static Logger LOGGER = LogManager.getLogger("Conduit");
    private static EventManager eventManager = new EventManager();
    private static PluginManager pluginManager = new PluginManager();
    private static CommandManager commandManager = new CommandManager();

    public static Logger getLogger() {
        return LOGGER;
    }

    public static EventManager getEventManager() {
        return eventManager;
    }

    public static PluginManager getPluginManager() {
        return pluginManager;
    }

    public static CommandManager getCommandManager() {
        return commandManager;
    }
}
