package me.ifydev.serverwrapper;

import me.ifydev.serverwrapper.events.EventManager;
import me.ifydev.serverwrapper.plugin.PluginLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Innectic
 * @since 10/2/2019
 */
public class ServerWrapper {

    public final static Logger LOGGER = LogManager.getLogger("ServerWrapper");
    public static EventManager eventManager = new EventManager();
    public static PluginLoader pluginLoader = new PluginLoader();
}
