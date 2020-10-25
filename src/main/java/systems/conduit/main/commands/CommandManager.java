package systems.conduit.main.commands;

import systems.conduit.main.Conduit;

import java.util.Arrays;

public class CommandManager {

    public void loadDefaultCommands() {
        Conduit.getServer().ifPresent(server -> {
            server.getCommands().getDispatcher().register(new ConduitCommand().getCommand());
        });
    }

    public void registerCommand(BaseCommand... commands) {
        Conduit.getServer().ifPresent(server -> Arrays.asList(commands).forEach(command -> server.getCommands().getDispatcher().register(command.getCommand())));
    }
}
