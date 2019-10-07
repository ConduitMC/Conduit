package me.ifydev.conduit;

import me.ifydev.conduit.events.EventManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Innectic
 * @since 10/2/2019
 */
public class Conduit {

    public final static Logger LOGGER = LogManager.getLogger("Conduit");
    public static EventManager eventManager = new EventManager();
}
