package systems.conduit.main;

import systems.conduit.main.events.EventManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Conduit {

    public final static Logger LOGGER = LogManager.getLogger("Conduit");
    public static EventManager eventManager = new EventManager();

}
