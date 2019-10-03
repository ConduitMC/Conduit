package me.ifydev.serverwrapper.events.annotations;

import me.ifydev.serverwrapper.events.EventType;

/**
 * @author Innectic
 * @since 10/2/2019
 */
public @interface EventHandler {
    Class<EventType> value();
}
