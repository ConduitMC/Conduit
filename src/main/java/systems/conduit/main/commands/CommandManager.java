package systems.conduit.main.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public class CommandManager {

    private static PluginCommand pluginCommand = new PluginCommand();

    public void loadDefaultCommands(CommandDispatcher<CommandSourceStack> dispatcher) {
        pluginCommand.registerCommand(dispatcher);
    }
}
