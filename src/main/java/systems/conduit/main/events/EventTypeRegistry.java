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
        eventMappings.add(WorldEvents.WorldSaveEvent.class);
        eventMappings.add(WorldEvents.WeatherClearEvent.class);
        eventMappings.add(WorldEvents.ThunderChangeStateEvent.class);
        eventMappings.add(WorldEvents.RainChangeStateEvent.class);
        eventMappings.add(WorldEvents.ChunkUnloadEvent.class);
        eventMappings.add(WorldEvents.ChunkLoadEvent.class);

        // Player events
        eventMappings.add(PlayerEvents.PlayerJoinEvent.class);
        eventMappings.add(PlayerEvents.PlayerLeaveEvent.class);
        eventMappings.add(PlayerEvents.PlayerDamageByEntityEvent.class);
        eventMappings.add(PlayerEvents.PlayerDamageByPlayerEvent.class);
        eventMappings.add(PlayerEvents.PlayerDamageByArrowEvent.class);
        eventMappings.add(PlayerEvents.PlayerGameModeChangeEvent.class);
        eventMappings.add(PlayerEvents.PlayerChatEvent.class);
        eventMappings.add(PlayerEvents.PlayerCommandEvent.class);
        eventMappings.add(PlayerEvents.ConsumeEvent.class);
        eventMappings.add(PlayerEvents.RespawnEvent.class);
        eventMappings.add(PlayerEvents.SpectateEvent.class);
        eventMappings.add(PlayerEvents.LevelSwitchEvent.class);
        eventMappings.add(PlayerEvents.InventoryMoveItemEvent.class);

        // Entity events
        eventMappings.add(EntityEvents.SheepGrowWoolEvent.class);
        eventMappings.add(EntityEvents.BabySheepEatEvent.class);
        eventMappings.add(EntityEvents.SlimeSplitEvent.class);
        eventMappings.add(EntityEvents.EffectAddedToEntityEvent.class);
        eventMappings.add(EntityEvents.EffectRemovedFromEntityEvent.class);
        eventMappings.add(EntityEvents.LevelSwitchEvent.class);

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
