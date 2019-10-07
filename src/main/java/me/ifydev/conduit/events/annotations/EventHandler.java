package me.ifydev.conduit.events.annotations;

import me.ifydev.conduit.events.EventType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Innectic
 * @since 10/2/2019
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {
    Class<? extends EventType> value();
}
