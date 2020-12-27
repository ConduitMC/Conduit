package systems.conduit.main.core.datastore.schema.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * @author Innectic
 * @since 12/24/2020
 */
public class SchemaSerializeUtil {

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

    public static Optional<Object> getValueOfField(Field field, Class<?> clazz, String method) {
        // First, see if we can even find a method that remotely looks like what we need
        List<Method> methods = CommonDatastoreUtil.findMethodByName(method, clazz);
        if (methods.isEmpty()) return Optional.empty();

        // We have at least one method, so now lets see if there's one that fits what we need.
        Optional<Method> meth = CommonDatastoreUtil.findMethodThatFitsRequirements(methods, field);
        if (!meth.isPresent()) return Optional.empty();

        // We do in fact have a method that fits the requirements to serialize. Now, lets actually try to serialize it.
        return trySerializeField(meth.get(), field, clazz);
    }
}
