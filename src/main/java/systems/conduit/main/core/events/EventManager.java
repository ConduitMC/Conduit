package systems.conduit.main.core.events;

import systems.conduit.main.Conduit;
import systems.conduit.main.core.events.annotations.Listener;
import systems.conduit.main.core.events.types.EventType;
import systems.conduit.main.core.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class EventManager {

    @SuppressWarnings("unchecked")
    private static Optional<Class<? extends EventType>> findEventTypeFromListenerMethod(Method method) {
        Parameter[] params = method.getParameters();
        if (params.length < 1) return Optional.empty();  // We have to have at least one parameter to check.

        // Check to make sure that this parameter is of the EventType type.
        Parameter param = params[0];
        if (!EventType.class.isAssignableFrom(param.getType())) return Optional.empty();

        return Optional.of((Class<? extends EventType>) param.getType());
    }

    public void registerEventClass(Plugin plugin, EventListener listener) {
        // Attempt to create an instance of the listener.
        if (listener == null) return;

        Method[] methods = listener.getClass().getMethods();
        for (Method method : methods) {
            // If this method is not annotated, then we don't care about it.
            if (!method.isAnnotationPresent(Listener.class)) continue;
            Listener annotation = method.getAnnotation(Listener.class);

            registerHandler(plugin, method, listener);
        }
    }

    private void registerHandler(Plugin plugin, Method method, EventListener listener) {
        // Attempt to get the type of event that this method is handling.
        Optional<Class<? extends EventType>> eventType = findEventTypeFromListenerMethod(method);
        if (!eventType.isPresent()) return;  // Skip this method as it doesn't look like it actually handles anything.

        int eventId = EventTypeRegistry.getEventMappings().indexOf(eventType.get());
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

        if (plugin.getEvents().get(eventId).containsKey(listener)) {
            // We have this class already!
            plugin.getEvents().get(eventId).get(listener).add(method);
            return;
        }

        plugin.getEvents().get(eventId).put(listener, Collections.singletonList(method));
    }

    public void dispatchEvent(EventType eventType) {
        int eventId = EventTypeRegistry.getEventMappings().indexOf(eventType.getClass());
        if (eventId == -1) {
            Conduit.getLogger().error("Cannot dispatch invalid event type: " + eventType.getClass());
            return;
        }

        // TODO: Maybe we should pre-process this garbage?
        Conduit.getPluginManager().getPlugins().forEach(plugin -> plugin.getEvents().getOrDefault(eventId, new HashMap<>()).entrySet().stream().sorted((o1, o2) -> {
            Listener firstAnnotation = o1.getKey().getClass().getAnnotation(Listener.class);
            Listener secondAnnotation = o2.getKey().getClass().getAnnotation(Listener.class);
            if (firstAnnotation == null || secondAnnotation == null) return 0;

            return Integer.compare(firstAnnotation.priority(), secondAnnotation.priority());
        }).forEach((entry) -> entry.getValue().forEach(method -> {
            try {
                method.invoke(entry.getKey(), eventType);
            } catch (IllegalAccessException | InvocationTargetException e) {
                Conduit.getLogger().error("Failed to execute plugin event handler method");
                e.printStackTrace();
            }
        })));
    }
}
