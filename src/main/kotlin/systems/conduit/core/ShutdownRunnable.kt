package systems.conduit.core

import kotlin.system.exitProcess

class ShutdownRunnable: Runnable {

    override fun run() {
        Conduit.pluginManager.disablePlugins()
        exitProcess(1)
    }
}
