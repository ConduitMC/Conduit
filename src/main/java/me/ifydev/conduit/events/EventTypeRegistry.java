package me.ifydev.conduit.events;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Innectic
 * @since 10/5/2019
 */
public class EventTypeRegistry {

    private static Map<Class<? extends EventType>, Integer> eventMappings = new HashMap<>();

    static {
        eventMappings.put(EventType.BlockInteractEvent.class, 1);
        eventMappings.put(EventType.BlockBreakEvent.class, 2);
        eventMappings.put(EventType.BlockPlaceEvent.class, 3);
        eventMappings.put(EventType.PlayerJoinEvent.class, 4);
        eventMappings.put(EventType.PlayerLeaveEvent.class, 5);
        eventMappings.put(EventType.PlayerDamageByEntityEvent.class, 6);
        eventMappings.put(EventType.PlayerDamageByPlayerEvent.class, 7);
        eventMappings.put(EventType.PlayerDamageByArrowEvent.class, 9);
    }

    public static Map<Class<? extends EventType>, Integer> getEventMappings() {
        return eventMappings;
    }
}
