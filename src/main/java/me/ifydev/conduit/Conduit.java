package me.ifydev.conduit;

import me.ifydev.conduit.events.EventManager;
import me.ifydev.conduit.plugin.PluginLoader;
import me.ifydev.conduit.plugin.PluginRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Innectic
 * @since 10/2/2019
 */
public class Conduit {

    public final static Logger LOGGER = LogManager.getLogger("Conduit");
    public static EventManager eventManager = new EventManager();
    public static PluginLoader pluginLoader = new PluginLoader();
    public static PluginRegistry pluginRegistry = new PluginRegistry();
}
