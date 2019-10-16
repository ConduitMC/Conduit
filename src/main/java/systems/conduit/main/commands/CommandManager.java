package systems.conduit.main.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public class CommandManager {

    private static PluginsCommand pluginsCommand = new PluginsCommand();

    public void loadDefaultCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        pluginsCommand.registerCommand(dispatcher);
    }
}
