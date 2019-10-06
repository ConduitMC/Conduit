package me.ifydev.serverwrapper.events;

import me.ifydev.serverwrapper.ServerWrapper;
import me.ifydev.serverwrapper.events.annotations.EventHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Innectic
 * @since 10/2/2019
 */
public class EventManager {

    private static Map<Integer, Map<EventListener, List<Method>>> registeredHandlers = new HashMap<>();  // TODO: Can this storage system be optimized?

    public void registerEventClass(Class<? extends EventListener> clazz) {
        // Attempt to create an instance of the listener.
        EventListener listener = null;
        try {
            listener = clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            ServerWrapper.LOGGER.error("Failed to create instance of event listener: " + clazz.getName());
            e.printStackTrace();
        }
        if (listener == null) return;

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            // If this method is not annotated, then we don't care about it.
            if (!method.isAnnotationPresent(EventHandler.class)) continue;
            EventHandler annotation = method.getAnnotation(EventHandler.class);

            registerHandler(method, listener, annotation);
        }
    }

    private void registerHandler(Method method, EventListener listener, EventHandler annotation) {
        Class<? extends EventType> eventType = annotation.value();
        int eventId = EventTypeRegistry.getEventMappings().getOrDefault(eventType, -1);
        if (eventId == -1) {
            ServerWrapper.LOGGER.error("Invalid event type: " + eventType);
            return;
        }

        if (!registeredHandlers.containsKey(eventId)) {
            // No currently registered events on this.
            Map<EventListener, List<Method>> handlers = new HashMap<>();
            List<Method> methods = new ArrayList<>();
            methods.add(method);
            handlers.put(listener, methods);
            registeredHandlers.put(eventId, handlers);
            return;
        }

        Map<EventListener, List<Method>> current = registeredHandlers.get(eventId);
        new HashMap<>(current).forEach((key, value) -> {
            System.out.println(key.getClass() + "" + listener.getClass());
            if (key.getClass() == listener.getClass()) {
                // If we found another handler that matches this one, then add to it.
                value.add(method);
                current.put(key, value);
                ServerWrapper.LOGGER.info("Registered handler: " + method.getName() + " " + listener.getClass());
            }
        });
        registeredHandlers.put(eventId, current);
    }

    public void dispatchEvent(EventType eventType) {
        int eventId = EventTypeRegistry.getEventMappings().getOrDefault(eventType.getClass(), -1);
        if (eventId == -1) {
            ServerWrapper.LOGGER.error("Invalid event type: " + eventType.getClass());
            return;
        }
        registeredHandlers.getOrDefault(eventId, new HashMap<>()).forEach((instance, methods) -> methods.forEach(method -> {
            try {
                method.invoke(instance, eventType);
            } catch (IllegalAccessException | InvocationTargetException e) {
                ServerWrapper.LOGGER.error("Failed to execute event handler method");
                e.printStackTrace();
            }
        }));
    }
}
