package systems.conduit.core.plugin.annotation

import systems.conduit.core.plugin.config.Configuration
import systems.conduit.core.plugin.config.NoConfig
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
annotation class PluginMeta(val name: String, val description: String = "", val version: String, val dependencies: Array<Dependency> = [],
                            val config: KClass<out Configuration> = NoConfig::class, val reloadable: Boolean = false)