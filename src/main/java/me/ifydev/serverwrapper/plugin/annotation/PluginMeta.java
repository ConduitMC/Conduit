package me.ifydev.serverwrapper.plugin.annotation;

/**
 * @author Innectic
 * @since 10/6/2019
 */
public @interface PluginMeta {

    String name();
    String description();
    String version();
    String author();
    Dependency[] dependencies() default {};
}
