package systems.conduit.main.plugin.annotation;

/**
 * @author Innectic
 * @since 10/6/2019
 */
public @interface Dependency {
    String name();
    DependencyType type();
}