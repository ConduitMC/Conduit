package systems.conduit.core.plugin.config.annotation

/*
 * @author Innectic
 * @since 10/17/2019
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class ConfigFile(val name: String, val type: String, val defaultFile: String = "") 