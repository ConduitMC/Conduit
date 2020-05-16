package systems.conduit.core.events.types

import systems.conduit.core.plugin.Plugin

/*
 * @author Innectic
 * @since 10/19/2019
 */
class ServerEvents {

    class ServerInitializedEvent: EventType()
    class ServerStartingEvent: EventType()
    class ServerShuttingDownEvent: EventType()

    class PluginReloadEvent(val plugin: Plugin): EventType()

}
