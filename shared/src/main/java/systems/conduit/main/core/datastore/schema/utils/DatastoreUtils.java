package systems.conduit.main.core.datastore.schema.utils;

import systems.conduit.main.Conduit;
import systems.conduit.main.core.datastore.Datastore;
import systems.conduit.main.core.datastore.schema.Schema;
import systems.conduit.main.core.datastore.schema.annotations.SchemaMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * Manages a plugin's datastores.
 *
 * @author Innectic
 * @since 12/31/2019
 */
public class DatastoreUtils {

    /**
     * Make sure that the name does not contain any invalid characters.
     *
     * @param name the name to check
     * @return if the name is valid or not.
     */
    private static boolean isInvalidName(String name) {
        return !name.chars().allMatch(v -> Character.isLetterOrDigit(v) || v == '_');
    }

    public static String cleanupSchemaName(String input) {
        return input.replaceAll("[sS]chema", "").toLowerCase();
    }

    public static String getNameOfSchema(Class<? extends Schema> schema) {
        return schema.isAnnotationPresent(SchemaMeta.class) ? schema.getAnnotation(SchemaMeta.class).value() : DatastoreUtils.cleanupSchemaName(schema.getSimpleName());
    }

    /**
     * Create a new instance of a datastore.
     *
     * @param clazz the datastore class to create
     * @return an instance of the datastore ready to be attached, empty if it failed.
     */
    public static Optional<Datastore> createNewHandlerInstance(Class<? extends Datastore> clazz) {
        try {
            Constructor<? extends Datastore> constructor = clazz.getConstructor();
            Datastore handler = constructor.newInstance();
            return Optional.of(handler);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            Conduit.getLogger().error("Failed to instantiate datastore handler!");
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
