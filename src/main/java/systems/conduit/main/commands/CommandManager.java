package systems.conduit.main.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public class CommandManager {

    public void loadDefaultCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        new PluginsCommand().registerCommand(dispatcher);
        new VersionCommand().registerCommand(dispatcher);
    }
}
