package systems.conduit.main.core.datastore.annotations;

import systems.conduit.main.core.datastore.NoFactoryClass;

/**
 * @author Innectic
 * @since 12/24/2020
 */
public @interface Field {

    String value();

    Class<?> factoryClazz() default NoFactoryClass.class;
    String factoryMethod() default "";
    String serializeMethod() default "";
}
