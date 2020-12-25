package systems.conduit.main.core.datastore.schema;

import systems.conduit.main.Conduit;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Innectic
 * @since 12/24/2020
 */
public abstract class Schema {

    public final Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();

        List<Field> schemaFields = Arrays.stream(this.getClass().getFields())
                .filter(f -> f.isAnnotationPresent(systems.conduit.main.core.datastore.schema.annotations.Field.class))
                .collect(Collectors.toList());

        for (Field field : schemaFields) {
            // We already know every field has the @Field annotation, so just get it so we can start working with it.
            systems.conduit.main.core.datastore.schema.annotations.Field fieldAnnotation = field.getAnnotation(systems.conduit.main.core.datastore.schema.annotations.Field.class);
            if (fieldAnnotation == null) continue;

            Object value = null;
            // Before attempting to get any value out of it, check if we need to use a custom serializer method.
            if (!fieldAnnotation.serializeMethod().isEmpty()) {
                // It isn't empty, so we're supposed to be using a custom serializer. Double check that a class was provided.
                if (fieldAnnotation.factoryClazz() == NoFactoryClass.class) {
                    Conduit.getLogger().error("Schema field '" + fieldAnnotation.value() + "' does not have a factory class for serialization!");
                    continue;
                }

                // We have a factory class, and we have a serializer method. Get the value.
                Optional<Object> result = SchemaFieldUtil.getValueOfField(field, fieldAnnotation.factoryClazz(), fieldAnnotation.serializeMethod());
                if (!result.isPresent()) {
                    // Failed to serialize the field.
                    // TODO: Put more error information here.
                    Conduit.getLogger().error("Schema field '" + fieldAnnotation.value() + "' failed to serialize in factory method!");
                    continue;
                }
                value = result.get();
            } else {
                // We don't have a custom serializer, so just get the value of the field.
                try {
                    value = field.get(this.getClass());
                } catch (IllegalAccessException ignored) { }
            }

            if (value == null) {
                // Failed to get the value.
                Conduit.getLogger().error("Schema field '" + fieldAnnotation.value() + "' did not contain data on serialize!");
                continue;
            }

            // Now we have the value of this field and it's ready to be committed.
            serialized.put(fieldAnnotation.value(), value);
        }
        return serialized;
    }
}
