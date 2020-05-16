package systems.conduit.core.console

import net.minecraft.DefaultUncaughtExceptionHandler
import org.jline.reader.EndOfFileException
import org.jline.reader.LineReaderBuilder
import org.jline.reader.UserInterruptException
import org.jline.reader.impl.DefaultParser
import org.jline.terminal.TerminalBuilder
import systems.conduit.api.MinecraftServer
import systems.conduit.core.Conduit
import java.io.IOException

object Console {
    private var consoleStarted = false

    // TODO: Add tab complete?
    fun createConsole() {
        if (consoleStarted) return
        consoleStarted = true
        try {
            val terminal = TerminalBuilder.builder().dumb(true).build()
            val reader = LineReaderBuilder.builder().terminal(terminal).parser(DefaultParser()).appName(Conduit.meta.name).build()
            val consoleThread: Thread = object: Thread("Server console handler") {
                override fun run() {
                    try {
                        var line: String?
                        while (Conduit.server.isRunning() && !Conduit.server.isStopped()) {
                            line = try {
                                reader.readLine("> ", null)
                            } catch (e: EndOfFileException) {
                                e.printStackTrace()
                                continue
                            }
                            if (line == null || line.trim { it <= ' ' }.isEmpty()) continue
                            Conduit.server.getCommands().performCommand(Conduit.server.createCommandSourceStack(), line)
                        }
                    } catch (e: UserInterruptException) {
                        // If the user sent a kill code to the process, stop the server.
                        Conduit.server.close()
                    }
                }
            }
            consoleThread.isDaemon = true
            consoleThread.uncaughtExceptionHandler = DefaultUncaughtExceptionHandler(Conduit.logger)
            consoleThread.start()
        } catch (e: IOException) {
            Conduit.logger.error("Failed to initialize terminal.")
            e.printStackTrace()
        }
    }
}