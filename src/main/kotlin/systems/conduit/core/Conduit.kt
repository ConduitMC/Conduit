package systems.conduit.core

import net.minecraft.network.chat.TextComponent
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import systems.conduit.api.MinecraftServer
import systems.conduit.core.commands.CommandManager
import systems.conduit.core.console.MessageFactory
import systems.conduit.core.events.EventManager
import systems.conduit.core.managers.BossBarManager
import systems.conduit.core.managers.LevelManager
import systems.conduit.core.managers.PlayerManager
import systems.conduit.core.plugin.PluginManager
import systems.conduit.core.plugin.annotation.PluginMeta
import systems.conduit.core.plugin.config.ConfigurationTypes
import java.io.IOException
import java.io.PrintStream
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@PluginMeta(name = "Conduit", version = "0.0.5")
object Conduit {

    val logger: Logger = LogManager.getLogger("Conduit", MessageFactory())
    val eventManager = EventManager()
    val pluginManager = PluginManager()
    val commandManager = CommandManager()
    val playerManager = PlayerManager()
    val levelManager = LevelManager()
    val bossBarManager = BossBarManager()
    lateinit var server: MinecraftServer

    // TODO: Replace with build number in future
    val meta = Conduit::class.java.getAnnotation(PluginMeta::class.java)

    private var configuration: ConduitConfiguration? = null

    fun setupLogger() {
        // Redirect print to logger
        System.setOut(createLoggingProxy(System.out))
        System.setErr(createLoggingProxy(System.err))
    }

    private fun createLoggingProxy(realPrintStream: PrintStream): PrintStream {
        return object: PrintStream(realPrintStream) {
            override fun print(string: String) {
                logger.info(TextComponent(string).text)
            }

            override fun println(string: String) {
                logger.info(TextComponent(string).text)
            }
        }
    }

    fun loadConfiguration() {
        // Ensure that the path exists. If it doesn't, then we don't need to process this.
        val file = Paths.get("conduit.json").toFile()
        if (!file.exists()) {
            // If the file does not exist, then lets attempt to generate a default.
            try {
                Conduit::class.java.getResourceAsStream("/conduit.json").use { stream ->   // TODO: Fix that separator some day
                    if (stream != null) Files.copy(stream, Paths.get("conduit.json"), StandardCopyOption.REPLACE_EXISTING)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                return
            }
        }
        val loader = ConfigurationTypes.getLoaderForExtension("json")
        if (loader == null) {
            logger.error("Failed to find loader for JSON extension")
            return
        }
        if (file.exists()) configuration = loader.load(file, ConduitConfiguration::class.java) as ConduitConfiguration
    }
}
