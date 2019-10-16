package systems.conduit.main.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public abstract class BaseCommand {
    public abstract void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher);

}
