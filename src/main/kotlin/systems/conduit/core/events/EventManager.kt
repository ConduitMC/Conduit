package systems.conduit.core.events

import systems.conduit.core.Conduit
import systems.conduit.core.events.annotations.Listener
import systems.conduit.core.events.types.EventType
import systems.conduit.core.events.EventTypeRegistry
import systems.conduit.core.plugin.Plugin
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class EventManager {

    fun registerEventClass(plugin: Plugin, clazz: Class<out EventListener>) {
        // Attempt to create an instance of the listener.
        var listener: EventListener? = null
        try {
            listener = clazz.getConstructor().newInstance()
        } catch (e: NoSuchMethodException) {
            Conduit.logger.error("Failed to create instance of event listener: " + clazz.name)
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            Conduit.logger.error("Failed to create instance of event listener: " + clazz.name)
            e.printStackTrace()
        } catch (e: InstantiationException) {
            Conduit.logger.error("Failed to create instance of event listener: " + clazz.name)
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            Conduit.logger.error("Failed to create instance of event listener: " + clazz.name)
            e.printStackTrace()
        }
        if (listener == null) return
        val methods = clazz.methods
        for (method in methods) {
            // If this method is not annotated, then we don't care about it.
            if (!method.isAnnotationPresent(Listener::class.java)) continue
            // TODO do we need this?
            val annotation = method.getAnnotation(Listener::class.java)
            registerHandler(plugin, method, listener)
        }
    }

    private fun registerHandler(plugin: Plugin, method: Method, listener: EventListener) {
        // Attempt to get the type of event that this method is handling
        // Skip this method if it doesn't look like it actually handles anythin.
        val eventType = findEventTypeFromListenerMethod(method) ?: return
        val eventId: Int = EventTypeRegistry.getEventMappings().indexOf(eventType)
        if (eventId == -1) {
            Conduit.logger.error("Invalid event type register: $eventType")
            return
        }
        if (!plugin.events.containsKey(eventId)) {
            // No currently registered events on this.
            val handlers: MutableMap<EventListener, MutableList<Method>> = HashMap()
            val methods: MutableList<Method> = ArrayList()
            methods.add(method)
            handlers[listener] = methods
            plugin.events[eventId] = handlers
            return
        }
        val current = plugin.events[eventId]!!
        HashMap(current).forEach { (key: EventListener, value: MutableList<Method>) ->
            if (key.javaClass == listener.javaClass) {
                // If we found another handler that matches this one, then add to it.
                value.add(method)
                current[key] = value
            }
        }
        plugin.events[eventId] = current
    }

    fun dispatchEvent(eventType: EventType) {
        val eventId: Int = EventTypeRegistry.getEventMappings().indexOf(eventType.javaClass)
        if (eventId == -1) {
            Conduit.logger.error("Cannot dispatch invalid event type: " + eventType.javaClass)
            return
        }

        // TODO: Maybe we should pre-process this garbage?
        Conduit.pluginManager.plugins.forEach { plugin ->
            plugin.events.getOrDefault(eventId, HashMap()).entries.stream().sorted { o1: Map.Entry<EventListener, List<Method?>?>, o2: Map.Entry<EventListener, List<Method?>?> ->
                val firstAnnotation = o1.key.javaClass.getAnnotation(Listener::class.java)
                val secondAnnotation = o2.key.javaClass.getAnnotation(Listener::class.java)
                if (firstAnnotation == null || secondAnnotation == null) return@sorted 0
                firstAnnotation.priority.compareTo(secondAnnotation.priority)
            }.forEach { entry: Map.Entry<EventListener?, List<Method>> ->
                entry.value.forEach { method ->
                    try {
                        method.invoke(entry.key, eventType)
                    } catch (e: IllegalAccessException) {
                        Conduit.logger.error("Failed to execute plugin event handler method")
                        e.printStackTrace()
                    } catch (e: InvocationTargetException) {
                        Conduit.logger.error("Failed to execute plugin event handler method")
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        private fun findEventTypeFromListenerMethod(method: Method): Class<out EventType>? {
            val params = method.parameters
            if (params.isEmpty()) return null // We have to have at least one parameter to check.
            // Check to make sure that this parameter is of the EventType type.
            val param = params[0]
            return if (!EventType::class.java.isAssignableFrom(param.type)) null else param.type as Class<out EventType>?
        }
    }
}
