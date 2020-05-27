package systems.conduit.core.commands

import systems.conduit.core.Conduit

class CommandManager {

    fun loadDefaultCommands() {
        //Conduit.server.getCommands().dispatcher.register(PluginsCommand().command)
        //Conduit.server.getCommands().dispatcher.register(VersionCommand().command)
    }

    fun registerCommand(vararg commands: BaseCommand?) {
        listOfNotNull(*commands).forEach { command -> Conduit.server.getCommands().dispatcher.register(command.command) }
    }
}
