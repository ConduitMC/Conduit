package systems.conduit.core.plugin.config

import java.io.File
import java.util.*

/*
 * @author Innectic
 * @since 10/16/2019
 */
interface ConfigurationLoader {
    fun load(file: File, configurationType: Class<out Configuration>): Configuration?
}
