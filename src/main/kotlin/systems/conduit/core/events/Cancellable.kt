package systems.conduit.core.events

import systems.conduit.core.events.types.EventType


/*
 * @author Innectic
 * @since 10/18/2019
 */
abstract class Cancellable(var cancelled: Boolean = false): EventType()
