package systems.conduit.main.events;

import systems.conduit.main.Conduit;
import systems.conduit.main.events.annotations.EventHandler;
import systems.conduit.main.events.types.EventType;
import systems.conduit.main.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class EventManager {

    public void registerEventClass(Plugin plugin, Class<? extends EventListener> clazz) {
        // Attempt to create an instance of the listener.
        EventListener listener = null;
        try {
            listener = clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            Conduit.getLogger().error("Failed to create instance of event listener: " + clazz.getName());
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
            Conduit.getLogger().error("Invalid event type register: " + eventType);
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
                Conduit.getLogger().info("Registered handler: " + method.getName() + " " + listener.getClass());
            }
        });
        plugin.getEvents().put(eventId, current);
    }

    public void dispatchEvent(EventType eventType) {
        int eventId = EventTypeRegistry.getEventMappings().indexOf(eventType.getClass());
        if (eventId == -1) {
            Conduit.getLogger().error("Invalid event type dispatch: " + eventType.getClass());
            return;
        }
        // TODO: Maybe we should pre-process this garbage?
        Conduit.getPluginManager().getPlugins().forEach(plugin -> plugin.getEvents().getOrDefault(eventId, new HashMap<>()).entrySet().stream().sorted((o1, o2) -> {
            EventHandler firstAnnotation = o1.getKey().getClass().getAnnotation(EventHandler.class);
            EventHandler secondAnnotation = o2.getKey().getClass().getAnnotation(EventHandler.class);
            if (firstAnnotation == null || secondAnnotation == null) return 0;

            return Integer.compare(firstAnnotation.priority(), secondAnnotation.priority());
        }).forEach((entry) -> entry.getValue().forEach(method -> {
            try {
                method.invoke(entry.getKey(), eventType);
            } catch (IllegalAccessException | InvocationTargetException e) {
                Conduit.getLogger().error("Failed to execute event handler method");
                e.printStackTrace();
            }
        })));
    }
}
