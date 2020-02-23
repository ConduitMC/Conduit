package systems.conduit.core.events.annotations;

import systems.conduit.core.events.types.EventType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Listener {
    Class<? extends EventType> value();
    int priority() default 1;
}
