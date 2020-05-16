package systems.conduit.core.plugin.config.loader

import com.google.gson.Gson
import systems.conduit.core.Conduit
import systems.conduit.core.plugin.config.Configuration
import systems.conduit.core.plugin.config.ConfigurationLoader
import systems.conduit.core.plugin.config.loader.JsonLoader.Companion.gson
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*

/*
 * @author Innectic
 * @since 10/16/2019
 */
class JsonLoader: ConfigurationLoader {

    override fun load(file: File, configurationType: Class<out Configuration>): Configuration? {
        try {
            return gson.fromJson(FileReader(file), configurationType)!!
        } catch (e: IOException) {
            Conduit.logger.error("Failed to load configuration file: $file")
            e.printStackTrace()
        }
        return null
    }

    companion object {
        private val gson = Gson()
    }
}
