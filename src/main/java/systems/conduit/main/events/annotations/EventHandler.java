package systems.conduit.main.events.annotations;

import systems.conduit.main.events.EventType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface EventHandler {
    Class<? extends EventType> value();
    int priority() default 1;
}
