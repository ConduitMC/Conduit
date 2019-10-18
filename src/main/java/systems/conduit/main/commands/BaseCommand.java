package systems.conduit.main.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;

public abstract class BaseCommand {

    public abstract LiteralArgumentBuilder<CommandSourceStack> getCommand();

}
