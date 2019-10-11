package systems.conduit.main.plugin.annotation;

public @interface Dependency {
    String name();
    DependencyType type();
}
