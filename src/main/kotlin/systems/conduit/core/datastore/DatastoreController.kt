package systems.conduit.core.datastore

import systems.conduit.core.Conduit
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * Manages a plugin's datastores.
 *
 * @author Innectic
 * @since 12/31/2019
 */
class DatastoreController(val pluginName: String) {

    private val handlers: MutableMap<String, DatastoreHandler?> = HashMap()

    /**
     * Make sure that the name does not contain any invalid characters.
     *
     * @param name the name to check
     * @return if the name is valid or not.
     */
    private fun isInvalidName(name: String): Boolean {
        return !name.chars().allMatch { v: Int -> Character.isLetterOrDigit(v) || v == '_'.toInt() }
    }

    /**
     * Create a new instance of a datastore.
     *
     * @param clazz the datastore class to create
     * @return an instance of the datastore ready to be attached, empty if it failed.
     */
    private fun createNewHandlerInstance(clazz: Class<out DatastoreHandler>): Optional<DatastoreHandler> {
        try {
            val constructor = clazz.getConstructor()
            val handler = constructor.newInstance()
            return Optional.of(handler)
        } catch (e: InstantiationException) {
            Conduit.logger.error("Failed to instantiate datastore handler!")
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            Conduit.logger.error("Failed to instantiate datastore handler!")
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            Conduit.logger.error("Failed to instantiate datastore handler!")
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            Conduit.logger.error("Failed to instantiate datastore handler!")
            e.printStackTrace()
        }
        return Optional.empty()
    }

    /**
     * Register a new datastore.
     *
     * @param name    the identifier for this datastore.
     * @param meta    information to be provided when attaching to the datastore.
     * @param backend the location that this datastore will be storing information
     */
    fun register(name: String, meta: MutableMap<String, Any>, backend: DatastoreBackend) {
        // Make sure the name is valid
        var identifier = name
        if (isInvalidName(identifier)) return
        identifier = "$pluginName-$identifier"
        meta["table"] = identifier

        // Once we have converted that, we need to make a new instance of the selected handler that we will be using for this store.
        val handler = createNewHandlerInstance(backend.handler)
        if (!handler.isPresent) {
            Conduit.logger.error("Failed to instantiate datastore handler!")
            return
        }
        handler.get().attach(meta)
        handlers[identifier] = handler.get()
    }

    /**
     * Delete a datastore and it's information.
     *
     * @param name the name of the datastore to delete
     */
    fun close(name: String) {
        if (isInvalidName(name)) return
        val handler = handlers.getOrDefault("$pluginName-$name", null) ?: return
        handler.detach()
        handlers.remove(name)
    }

    /**
     * Get a plugin's datastore by name.
     *
     * @param name   the name of the datastore
     * @return       the datastore, if present. Empty otherwise.
     */
    operator fun get(name: String): Optional<DatastoreHandler> {
        return if (isInvalidName(name)) Optional.empty() else Optional.ofNullable(handlers.getOrDefault("$pluginName-$name", null))
    }
}
