package systems.conduit.main.events;

import systems.conduit.main.events.types.*;

import java.util.ArrayList;
import java.util.List;

public class EventTypeRegistry {

    private static List<Class<? extends EventType>> eventMappings = new ArrayList<>();

    static {
        // World events
        eventMappings.add(WorldEvents.BlockInteractEvent.class);
        eventMappings.add(WorldEvents.BlockBreakEvent.class);
        eventMappings.add(WorldEvents.BlockPlaceEvent.class);

        // Player events
        eventMappings.add(PlayerEvents.PlayerJoinEvent.class);
        eventMappings.add(PlayerEvents.PlayerLeaveEvent.class);
        eventMappings.add(PlayerEvents.PlayerDamageByEntityEvent.class);
        eventMappings.add(PlayerEvents.PlayerDamageByPlayerEvent.class);
        eventMappings.add(PlayerEvents.PlayerDamageByArrowEvent.class);
        eventMappings.add(PlayerEvents.PlayerGameModeChangeEvent.class);
        eventMappings.add(PlayerEvents.PlayerChatEvent.class);
        eventMappings.add(PlayerEvents.PlayerCommandEvent.class);

        // Entity events
        eventMappings.add(EntityEvents.SheepGrowWoolEvent.class);
        eventMappings.add(EntityEvents.BabySheepEatEvent.class);
        eventMappings.add(EntityEvents.SlimeSplitEvent.class);

        // Server events
        eventMappings.add(ServerEvents.ServerInitializedEvent.class);
        eventMappings.add(ServerEvents.ServerStartingEvent.class);
        eventMappings.add(ServerEvents.ServerShuttingDownEvent.class);
        eventMappings.add(ServerEvents.PluginReloadEvent.class);
    }

    public static List<Class<? extends EventType>> getEventMappings() {
        return eventMappings;
    }
}
