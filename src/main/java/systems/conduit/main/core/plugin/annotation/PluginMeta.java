package systems.conduit.main.core.plugin.annotation;

import systems.conduit.main.core.plugin.config.Configuration;
import systems.conduit.main.core.plugin.config.NoConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PluginMeta {

    String name();
    String description();
    String version();
    String author();
    Dependency[] dependencies() default {};
    Class<? extends Configuration> config() default NoConfig.class;
    boolean reloadable() default false;

}
