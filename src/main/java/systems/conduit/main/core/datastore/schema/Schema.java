package systems.conduit.main.core.datastore.schema;

import systems.conduit.main.Conduit;
import systems.conduit.main.core.datastore.schema.utils.SchemaSerializeUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Innectic
 * @since 12/24/2020
 */
public abstract class Schema {

    public final Map<String, Object> serialize() {
        System.out.println("A");
        Map<String, Object> serialized = new HashMap<>();

        List<Field> schemaFields = Arrays.stream(getClass().getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(systems.conduit.main.core.datastore.schema.annotations.Field.class))
                .collect(Collectors.toList());
        System.out.println("A");

        for (Field field : schemaFields) {
            System.out.println("A " + field.getName());
            // We already know every field has the @Field annotation, so just get it so we can start working with it.
            systems.conduit.main.core.datastore.schema.annotations.Field fieldAnnotation = field.getAnnotation(systems.conduit.main.core.datastore.schema.annotations.Field.class);
            if (fieldAnnotation == null) continue;

            // Before attempting to get any value out of it, check if we need to use a custom serializer method.
            if (!fieldAnnotation.serializeMethod().equals("")) {
                // It isn't empty, so we're supposed to be using a custom serializer. Double check that a class was provided.
                if (fieldAnnotation.factoryClazz() == NoFactoryClass.class) {
                    Conduit.getLogger().error("Schema field '" + fieldAnnotation.value() + "' does not have a factory class for serialization!");
                    continue;
                }

                // We have a factory class, and we have a serializer method. Get the value.
                Optional<Object> result = SchemaSerializeUtil.getValueOfField(field, fieldAnnotation.factoryClazz(), fieldAnnotation.serializeMethod());
                if (!result.isPresent()) {
                    // Failed to serialize the field.
                    // TODO: Put more error information here.
                    Conduit.getLogger().error("Schema field '" + fieldAnnotation.value() + "' failed to serialize in factory method!");
                    continue;
                }
                serialized.put(fieldAnnotation.value(), result.get());

            } else {
                // We don't have a custom serializer, so just get the value of the field.
                try {
                    field.setAccessible(true);
                    serialized.put(fieldAnnotation.value(), field.get(this));
                } catch (IllegalAccessException ignored) { ignored.printStackTrace(); }
            }
        }
        return serialized;
    }
}
