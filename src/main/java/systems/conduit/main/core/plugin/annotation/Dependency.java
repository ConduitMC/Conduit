package systems.conduit.main.core.plugin.annotation;

public @interface Dependency {
    String name();
    DependencyType type();
}
