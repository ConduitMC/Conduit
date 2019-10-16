package systems.conduit.main.events;

import systems.conduit.main.Conduit;
import systems.conduit.main.events.annotations.EventHandler;
import systems.conduit.main.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

    public void registerEventClass(Plugin plugin, Class<? extends EventListener> clazz) {
        // Attempt to create an instance of the listener.
        EventListener listener = null;
        try {
            listener = clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            Conduit.LOGGER.error("Failed to create instance of event listener: " + clazz.getName());
            e.printStackTrace();
        }
        if (listener == null) return;

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            // If this method is not annotated, then we don't care about it.
            if (!method.isAnnotationPresent(EventHandler.class)) continue;
            EventHandler annotation = method.getAnnotation(EventHandler.class);
            registerHandler(plugin, method, listener, annotation);
        }
    }

    private void registerHandler(Plugin plugin, Method method, EventListener listener, EventHandler annotation) {
        Class<? extends EventType> eventType = annotation.value();
        int eventId = EventTypeRegistry.getEventMappings().indexOf(eventType);
        if (eventId == -1) {
            Conduit.LOGGER.error("Invalid event type register: " + eventType);
            return;
        }

        if (!plugin.getEvents().containsKey(eventId)) {
            // No currently registered events on this.
            Map<EventListener, List<Method>> handlers = new HashMap<>();
            List<Method> methods = new ArrayList<>();
            methods.add(method);
            handlers.put(listener, methods);
            plugin.getEvents().put(eventId, handlers);
            return;
        }

        Map<EventListener, List<Method>> current = plugin.getEvents().get(eventId);
        new HashMap<>(current).forEach((key, value) -> {
            if (key.getClass() == listener.getClass()) {
                // If we found another handler that matches this one, then add to it.
                value.add(method);
                current.put(key, value);
                Conduit.LOGGER.info("Registered handler: " + method.getName() + " " + listener.getClass());
            }
        });
        plugin.getEvents().put(eventId, current);
    }

    public void dispatchEvent(EventType eventType) {
        int eventId = EventTypeRegistry.getEventMappings().indexOf(eventType.getClass());
        if (eventId == -1) {
            Conduit.LOGGER.error("Invalid event type dispatch: " + eventType.getClass());
            return;
        }
        Conduit.pluginManager.getPlugins().forEach(plugin -> plugin.getEvents().getOrDefault(eventId, new HashMap<>()).forEach((instance, methods) -> methods.forEach(method -> {
            try {
                method.invoke(instance, eventType);
            } catch (IllegalAccessException | InvocationTargetException e) {
                Conduit.LOGGER.error("Failed to execute event handler method");
                e.printStackTrace();
            }
        })));
    }
}
