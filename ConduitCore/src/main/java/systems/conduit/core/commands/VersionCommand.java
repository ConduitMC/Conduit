package systems.conduit.core.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.SharedConstants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import systems.conduit.core.Conduit;
import systems.conduit.core.plugin.Plugin;

import java.util.Optional;

public class VersionCommand extends BaseCommand {

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("version").then(Commands.argument("pluginName", StringArgumentType.word()).executes(c -> {
            String pluginName = StringArgumentType.getString(c, "pluginName");
            Optional<Plugin> plugin = Conduit.getPluginManager().getPlugin(pluginName);
            if (plugin.isPresent()) {
                pluginName = plugin.get().getMeta().name();
                String version = plugin.get().getMeta().version();
                c.getSource().sendSuccess(new TextComponent(pluginName + " v" + version), false);
            } else {
                c.getSource().sendFailure(new TextComponent(pluginName + " is not a plugin!"));
            }
            return 1;
        })).executes(c -> {
            c.getSource().sendSuccess(new TextComponent(Conduit.getMeta().name()).append(" v").append(Conduit.getMeta().version()), false);
            c.getSource().sendSuccess(new TextComponent("Minecraft Server v" + SharedConstants.getCurrentVersion().getName().split("/")[0]), false);
            return 1;
        });
    }
}
