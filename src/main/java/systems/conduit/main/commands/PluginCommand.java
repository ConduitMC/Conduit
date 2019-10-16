package systems.conduit.main.commands;

import com.mojang.brigadier.CommandDispatcher;
import javassist.tools.Callback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import systems.conduit.main.Conduit;

public class PluginCommand extends BaseCommand {

    @Override
    public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("reload").executes(c -> {
            c.getSource().sendSuccess(new TextComponent("Server reloading..."), false);
            Conduit.getPluginManager().reloadPlugins(new Callback("Reload callback") {
                @Override
                public void result(Object[] objects) {
                    c.getSource().sendSuccess(new TextComponent("Server reloaded!"), false);
                }
            });
            return 1;
        }));
    }
}
