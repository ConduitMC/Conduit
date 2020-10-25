package systems.conduit.main.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.SharedConstants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import systems.conduit.main.Conduit;
import systems.conduit.main.commands.conduit.DimensionsCommand;
import systems.conduit.main.commands.conduit.PluginsCommand;
import systems.conduit.main.commands.conduit.VersionCommand;

/**
 * @author Innectic
 * @since 10/25/2020
 */
public class ConduitCommand extends BaseCommand {

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("conduit").executes(c -> {
            c.getSource().sendSuccess(new TextComponent(Conduit.getMeta().name()).append(" v").append(Conduit.getMeta().version()), false);
            c.getSource().sendSuccess(new TextComponent("Minecraft Server v" + SharedConstants.getCurrentVersion().getName().split("/")[0]), false);
            return 1;
        })
        .then(PluginsCommand.baseCommand().then(PluginsCommand.reloadSubcommand()).then(PluginsCommand.changeStateSubcommand(true))
                .then(PluginsCommand.changeStateSubcommand(false)))
        .then(VersionCommand.getCommand())
        .then(DimensionsCommand.baseCommand().then(DimensionsCommand.listCommand()).then(DimensionsCommand.teleportCommand()));
    }
}
