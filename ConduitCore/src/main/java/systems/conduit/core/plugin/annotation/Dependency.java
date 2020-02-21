package systems.conduit.core.plugin.annotation;

public @interface Dependency {
    String name();
    DependencyType type();
}
