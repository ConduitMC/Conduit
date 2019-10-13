package systems.conduit.main.events;

import java.util.ArrayList;
import java.util.List;

public class EventTypeRegistry {

    private static List<Class<? extends EventType>> eventMappings = new ArrayList<>();

    static {
        eventMappings.add(EventType.BlockInteractEvent.class);
        eventMappings.add(EventType.BlockBreakEvent.class);
        eventMappings.add(EventType.BlockPlaceEvent.class);
        eventMappings.add(EventType.PlayerJoinEvent.class);
        eventMappings.add(EventType.PlayerLeaveEvent.class);
        eventMappings.add(EventType.PlayerDamageByEntityEvent.class);
        eventMappings.add(EventType.PlayerDamageByPlayerEvent.class);
        eventMappings.add(EventType.PlayerDamageByArrowEvent.class);
        eventMappings.add(EventType.PlayerGameModeChangeEvent.class);
        eventMappings.add(EventType.PlayerChatEvent.class);
        eventMappings.add(EventType.PlayerCommandEvent.class);
    }

    public static List<Class<? extends EventType>> getEventMappings() {
        return eventMappings;
    }
}
