package systems.conduit.core.commands;

import systems.conduit.core.Conduit;

import java.util.Arrays;

public class CommandManager {

    public void loadDefaultCommands() {
        Conduit.getServer().ifPresent(server -> {
            server.getCommands().getDispatcher().register(new PluginsCommand().getCommand());
            server.getCommands().getDispatcher().register(new VersionCommand().getCommand());
        });
    }

    public void registerCommand(BaseCommand... commands) {
        Conduit.getServer().ifPresent(server -> Arrays.asList(commands).forEach(command -> server.getCommands().getDispatcher().register(command.getCommand())));
    }
}
