package systems.conduit.main.core.datastore.schema.utils;

import systems.conduit.main.Conduit;
import systems.conduit.main.core.datastore.schema.Schema;
import systems.conduit.main.core.datastore.schema.types.MySQLTypes;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Innectic
 * @since 12/25/2020
 */
public class DatabaseSchemaUtil {

    private static MySQLTypes convertType(Field field) {
        Class<?> type = field.getType();

        return Arrays.stream(MySQLTypes.values()).filter(t -> t.getClazz().equals(type)).findFirst().orElse(MySQLTypes.INVALID);
    }

    public static Map<String, MySQLTypes> convertSchemaToMySQLSchema(Class<? extends Schema> schema) {
        Map<String, MySQLTypes> convertedSchema = new HashMap<>();

        Field[] fields = schema.getFields();

        for (Field field : fields) {
            if (!field.isAnnotationPresent(systems.conduit.main.core.datastore.schema.annotations.Field.class)) continue;

            // First, try to get the mysql type for this
            MySQLTypes type = convertType(field);

            // Ensure that this is a valid type
            if (type.equals(MySQLTypes.INVALID)) {
                // Invalid type.
                Conduit.getLogger().error("MySQL schema conversion error: '" + field.getType().getName() + "' is not convertable!");
                // TODO: Look at serialization methods for return type, or just store the actual class
                continue;
            }

            convertedSchema.put(CommonDatastoreUtil.fieldLabel(field, field.getAnnotation(systems.conduit.main.core.datastore.schema.annotations.Field.class)), type);
        }

        return convertedSchema;
    }
}
