package systems.conduit.core.events

import systems.conduit.core.events.types.*
import java.util.*

object EventTypeRegistry {
    private val eventMappings: MutableList<Class<out EventType>> = ArrayList()
    fun getEventMappings(): List<Class<out EventType>> {
        return eventMappings
    }

    init {
        // World events
        eventMappings.add(WorldEvents.BlockInteractEvent::class.java)
        eventMappings.add(WorldEvents.BlockBreakEvent::class.java)
        eventMappings.add(WorldEvents.BlockPlaceEvent::class.java)
        eventMappings.add(WorldEvents.WorldSaveEvent::class.java)

        // Player events
        eventMappings.add(PlayerEvents.PlayerJoinEvent::class.java)
        eventMappings.add(PlayerEvents.PlayerLeaveEvent::class.java)
        eventMappings.add(PlayerEvents.PlayerDamageByEntityEvent::class.java)
        eventMappings.add(PlayerEvents.PlayerDamageByPlayerEvent::class.java)
        eventMappings.add(PlayerEvents.PlayerDamageByArrowEvent::class.java)
        eventMappings.add(PlayerEvents.PlayerGameModeChangeEvent::class.java)
        eventMappings.add(PlayerEvents.PlayerChatEvent::class.java)
        eventMappings.add(PlayerEvents.PlayerCommandEvent::class.java)
        eventMappings.add(PlayerEvents.ConsumeEvent::class.java)

        // Entity events
        eventMappings.add(EntityEvents.SheepGrowWoolEvent::class.java)
        eventMappings.add(EntityEvents.BabySheepEatEvent::class.java)
        eventMappings.add(EntityEvents.SlimeSplitEvent::class.java)
        eventMappings.add(EntityEvents.EffectAddedToEntityEvent::class.java)
        eventMappings.add(EntityEvents.EffectRemovedFromEntityEvent::class.java)

        // Server events
        eventMappings.add(ServerEvents.ServerInitializedEvent::class.java)
        eventMappings.add(ServerEvents.ServerStartingEvent::class.java)
        eventMappings.add(ServerEvents.ServerShuttingDownEvent::class.java)
        eventMappings.add(ServerEvents.PluginReloadEvent::class.java)
    }
}