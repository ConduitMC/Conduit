package systems.conduit.main.core.datastore.schema.utils;

import systems.conduit.main.core.datastore.schema.Schema;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * @author Innectic
 * @since 12/25/2020
 */
public class SchemaDeserializeUtil {

    public static Optional<Schema> tryCreateNewSchema(Class<? extends Schema> clazz) {
        try {
            Constructor<? extends Schema> constructor = clazz.getConstructor();
            return Optional.of(constructor.newInstance());
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ignored) {
        }
        return Optional.empty();
    }

    private static Optional<Object> tryFitFromFactory(Method method, Object data) {
        try {
            return Optional.ofNullable(method.invoke(null, data));
        } catch (IllegalAccessException | InvocationTargetException ignored) { }
        return Optional.empty();
    }

    public static Optional<Object> feedFactoryMethod(Class<?> clazz, String method, Object data) {
        // First, see if we can even find a method that remotely looks like what we need
        List<Method> methods = CommonDatastoreUtil.findMethodByName(method, clazz);
        if (methods.isEmpty()) return Optional.empty();

        // We have at least one method, so now lets see if there's one that fits what we need.
        Optional<Method> meth = CommonDatastoreUtil.findMethodThatFitsRequirements(methods, data.getClass());
        if (!meth.isPresent()) return Optional.empty();

        // We found a method that looks like what we need. Attempt to fit it.
        return tryFitFromFactory(meth.get(), data);
    }
}
