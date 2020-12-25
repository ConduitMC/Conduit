package systems.conduit.main.core.datastore.schema.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Innectic
 * @since 12/25/2020
 */
public class CommonDatastoreUtil {

    /**
     * Find a function on the target serializer class WITHOUT looking at arguments.
     *
     * @param name the name of the function to find
     * @param on the class that should contain it
     * @return the method, if it exists.
     */
    public static List<Method> findMethodByName(String name, Class<?> on) {
        return Arrays.stream(on.getMethods())
                .filter(m -> m.getName().equals(name))
                .collect(Collectors.toList());
    }

    public static Optional<Method> findMethodThatFitsRequirements(List<Method> methods, Field field) {
        return findMethodThatFitsRequirements(methods, field.getDeclaringClass());
    }

    public static Optional<Method> findMethodThatFitsRequirements(List<Method> methods, Class<?> clazz) {
        return methods.stream()
                .filter(m -> m.getParameters().length == 1)
                .filter(m -> m.getParameters()[0].getType() == clazz)
                .findFirst();
    }
}
