package me.ifydev.serverwrapper.events;

import me.ifydev.serverwrapper.ServerWrapper;
import me.ifydev.serverwrapper.events.annotations.EventHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Innectic
 * @since 10/2/2019
 */
public class EventManager {

    private Map<Integer, Map<EventListener, Method>> registeredHandlers = new HashMap<>();  // TODO: Can this storage system be optimized?

    public void registerEventClass(Class<? extends EventListener> clazz) {
        Method[] methods = clazz.getMethods();

        for (Method method : methods) {
            // If this method is not annotated, then we don't care about it.
            if (!method.isAnnotationPresent(EventHandler.class)) continue;
            EventHandler annotation = method.getAnnotation(EventHandler.class);

            registerHandler(method, annotation);
        }
    }

    private void registerHandler(Method method, EventHandler annotation) {
        Class<? extends EventType> eventType = annotation.value();
    }

    public void dispatchEvent(EventType eventType) {
        int eventId = eventType.getId();
        registeredHandlers.getOrDefault(eventId, new HashMap<>()).forEach((instance, method) -> {
            try {
                method.invoke(instance, eventType);
            } catch (IllegalAccessException | InvocationTargetException e) {
                ServerWrapper.LOGGER.error("Failed to execute event handler method");
                e.printStackTrace();
            }
        });
    }
}
