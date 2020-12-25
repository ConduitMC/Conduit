package systems.conduit.main.core.datastore.schema.annotations;

import systems.conduit.main.core.datastore.schema.NoFactoryClass;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Innectic
 * @since 12/24/2020
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Field {

    String value();

    Class<?> factoryClazz() default NoFactoryClass.class;
    String factoryMethod() default "";
    String serializeMethod() default "";
}
