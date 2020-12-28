package systems.conduit.main.core.datastore.schema;

import lombok.Getter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.datastore.schema.utils.CommonDatastoreUtil;
import systems.conduit.main.core.datastore.schema.utils.SchemaDeserializeUtil;
import systems.conduit.main.core.datastore.schema.utils.SchemaSerializeUtil;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Innectic
 * @since 12/24/2020
 */
public abstract class Schema {

    // Auto generated and incremented by the database.
    @systems.conduit.main.core.datastore.schema.annotations.Field @Getter private int id;

    public final Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();

        List<Field> schemaFields = getAllFields(getClass(), new ArrayList<>()).stream()
                .filter(f -> f.isAnnotationPresent(systems.conduit.main.core.datastore.schema.annotations.Field.class))
                .collect(Collectors.toList());

        for (Field field : schemaFields) {
            // We already know every field has the @Field annotation, so just get it so we can start working with it.
            systems.conduit.main.core.datastore.schema.annotations.Field fieldAnnotation = field.getAnnotation(systems.conduit.main.core.datastore.schema.annotations.Field.class);
            if (fieldAnnotation == null) continue;

            String fieldLabel = CommonDatastoreUtil.fieldLabel(field, fieldAnnotation);

            // Before attempting to get any value out of it, check if we need to use a custom serializer method.
            if (!fieldAnnotation.serializeMethod().equals("")) {
                // It isn't empty, so we're supposed to be using a custom serializer. Double check that a class was provided.
                if (fieldAnnotation.factoryClazz() == NoFactoryClass.class) {
                    Conduit.getLogger().error("Schema field '" + fieldLabel + "' does not have a factory class for serialization!");
                    continue;
                }

                // We have a factory class, and we have a serializer method. Get the value.
                Optional<Object> result = SchemaSerializeUtil.getValueOfField(field, fieldAnnotation.factoryClazz(), fieldAnnotation.serializeMethod());
                if (!result.isPresent()) {
                    // Failed to serialize the field.
                    // TODO: Put more error information here.
                    Conduit.getLogger().error("Schema field '" + fieldLabel + "' failed to serialize in factory method!");
                    continue;
                }
                serialized.put(fieldLabel, result.get());

            } else {
                // We don't have a custom serializer, so just get the value of the field.
                try {
                    field.setAccessible(true);
                    serialized.put(fieldLabel, field.get(this));
                } catch (IllegalAccessException ignored) { }
            }
        }
        return serialized;
    }

    public static List<Field> getAllFields(Class<?> clazz, List<Field> last) {
        last.addAll(Arrays.asList(clazz.getDeclaredFields()));

        if (clazz.getSuperclass() != null) getAllFields(clazz.getSuperclass(), last);

        return last;
    }

    public static <T extends Schema> Optional<T> of(Class<? extends Schema> clazz, Map<String, Object> data) {
        // First create a new schema.
        Optional<Schema> schema = SchemaDeserializeUtil.tryCreateNewSchema(clazz);
        if (!schema.isPresent()) return Optional.empty();
        T actualSchema = (T) schema.get();

        List<Field> fields = getAllFields(actualSchema.getClass(), new ArrayList<>()).stream().filter(m -> m.isAnnotationPresent(systems.conduit.main.core.datastore.schema.annotations.Field.class)).collect(Collectors.toList());

        for (Field field : fields) {
            systems.conduit.main.core.datastore.schema.annotations.Field fieldAnnotation = field.getAnnotation(systems.conduit.main.core.datastore.schema.annotations.Field.class);
            String fieldLabel = CommonDatastoreUtil.fieldLabel(field, fieldAnnotation);

            // Check if there is a data entry for this field name
            if (!data.containsKey(fieldLabel)) continue;

            // We do in fact have data for this key. Now we have to attempt to fill the field.
            Object currentValue = data.get(fieldLabel);

            ItemStack item = new ItemStack(Items.DIAMOND_PICKAXE);
            item.hurt(100, new Random(), null);
            // Now, check to see if we have a custom data deserializer.
            if (!fieldAnnotation.factoryMethod().equals("")) {
                // It isn't empty, so we're supposed to be using a custom serializer. Double check that a class was provided.
                if (fieldAnnotation.factoryClazz() == NoFactoryClass.class) {
                    Conduit.getLogger().error("Schema field '" + fieldLabel + "' does not have a factory class for serialization!");
                    continue;
                }
                // We do in fact have a custom deserializer. Feed this data to it.
                Optional<Object> result = SchemaDeserializeUtil.feedFactoryMethod(fieldAnnotation.factoryClazz(), fieldAnnotation.factoryMethod(), currentValue);
                if (!result.isPresent()) {
                    // Failed to get data back from the factory method.
                    Conduit.getLogger().error("Schema field '" + fieldLabel + "' failed to be converted in the factory!");
                    continue;
                }
                currentValue = result.get();
            }

            // Now that we have the final data, lets try to fit it into the field.
            try {
                // HACK?
                field.setAccessible(true);
                field.set(actualSchema, currentValue);
            } catch (IllegalAccessException e) { e.printStackTrace(); }
        }

        return Optional.of(actualSchema);
    }
}
