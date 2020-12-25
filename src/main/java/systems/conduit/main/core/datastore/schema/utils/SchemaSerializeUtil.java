package systems.conduit.main.core.datastore.schema.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Innectic
 * @since 12/24/2020
 */
public class SchemaSerializeUtil {

    /**
     * Find a function on the target serializer class WITHOUT looking at arguments.
     *
     * @param name the name of the function to find
     * @param on the class that should contain it
     * @return the method, if it exists.
     */
    private static List<Method> findMethodByName(String name, Class<?> on) {
        return Arrays.stream(on.getMethods())
                .filter(m -> m.getName().equals(name))
                .collect(Collectors.toList());
    }

    private static Optional<Method> findMethodThatFitsRequirements(List<Method> methods, Field field) {
        return methods.stream()
                .filter(m -> m.getTypeParameters().length == 1)
                .filter(m -> m.getParameters()[0].getClass() == field.getDeclaringClass())
                .findFirst();
    }

    private static Optional<Object> trySerializeField(Method method, Field field, Class<?> clazz) {
        // Firs pull the value
        Object value;
        try {
            value = field.get(clazz);
        } catch (IllegalAccessException e) {
            return Optional.empty();
        }
        if (value == null) return Optional.empty();

        // Since we now have the value, make sure we an fit it into the type of the method
        if (!value.getClass().isAssignableFrom(clazz)) return Optional.empty();

        // Now we know it can be assigned from our target, so we can actually fit this into the method now.
        Object result;
        try {
            result = method.invoke(null, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return Optional.empty();
        }

        // Now we have a result. This result should represent whatever type that we need to store it into the datastore.
        return Optional.ofNullable(result);
    }

    static Optional<Object> getValueOfField(Field field, Class<?> clazz, String method) {
        // First, see if we can even find a method that remotely looks like what we need
        List<Method> methods = findMethodByName(method, clazz);
        if (methods.isEmpty()) return Optional.empty();

        // We have at least one method, so now lets see if there's one that fits what we need.
        Optional<Method> meth = findMethodThatFitsRequirements(methods, field);
        if (!meth.isPresent()) return Optional.empty();

        // We do in fact have a method that fits the requirements to serialize. Now, lets actually try to serialize it.
        return trySerializeField(meth.get(), field, clazz);
    }
}
