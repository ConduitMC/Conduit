package systems.conduit.main.plugin.annotation;

import systems.conduit.main.plugin.config.Configuration;
import systems.conduit.main.plugin.config.NoConfig;

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
