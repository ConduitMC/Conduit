package systems.conduit.main.events;

/**
 * @author Innectic
 * @since 10/19/2019
 */
public class ServerEvents {

    public static class ServerInitializedEvent extends EventType { }

    public static class ServerStartingEvent extends EventType { }

    public static class ServerShuttingDownEvent extends EventType { }
}
