package systems.conduit.core.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import javassist.tools.Callback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import systems.conduit.core.Conduit;
import systems.conduit.core.plugin.Plugin;

import java.util.Optional;

public class PluginsCommand extends BaseCommand {

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return baseCommand().then(reloadSubcommand()).then(changeStateSubcommand(true)).then(changeStateSubcommand(false));
    }

    private LiteralArgumentBuilder<CommandSourceStack> baseCommand() {
        return Commands.literal("plugins").executes(c -> {
            Conduit.getPluginManager().getPlugins().stream().map(Plugin::toStringColored).reduce((a, b) -> a.concat(",").concat(b)).ifPresent(s -> {
                c.getSource().sendSuccess(new TextComponent("Plugins: " + s), false);
            });
            return 1;
        });
    }

    private LiteralArgumentBuilder<CommandSourceStack> reloadSubcommand() {
        return Commands.literal("reload").then(Commands.argument("pluginName", StringArgumentType.word()).executes(c -> {
            String pluginName = StringArgumentType.getString(c, "pluginName");
            Optional<Plugin> plugin = Conduit.getPluginManager().getPlugin(pluginName);
            if (plugin.isPresent()) {
                if (!plugin.get().getMeta().reloadable()) {
                    // This systems.conduit.core.plugin is not reloadable. Let the user know.
                    c.getSource().sendFailure(new TextComponent(pluginName + " is not reloadable."));
                    return 0;
                }
                pluginName = plugin.get().getMeta().name();
                c.getSource().sendSuccess(new TextComponent("Reloading systems.conduit.core.plugin: " + pluginName), false);
                Conduit.getPluginManager().reload(plugin.get(), c.getSource().getEntity() == null);
                c.getSource().sendSuccess(new TextComponent("Reloaded systems.conduit.core.plugin: " + pluginName), false);
            } else {
                c.getSource().sendFailure(new TextComponent(pluginName + " is not a systems.conduit.core.plugin. Unable to reload!"));
            }
            return 1;
        })).executes(c -> {
            c.getSource().sendSuccess(new TextComponent("Reloading all plugins..."), false);
            Conduit.getPluginManager().reloadPlugins(c.getSource().getEntity() == null, new Callback("Reload callback") {
                @Override
                public void result(Object[] objects) {
                    c.getSource().sendSuccess(new TextComponent("Reloaded all plugins"), false);
                }
            });
            return 1;
        });
    }

    private LiteralArgumentBuilder<CommandSourceStack> changeStateSubcommand(boolean enable) {
        String state = (enable ? "enable" : "disable");
        String preStateText = (enable ? "Enabling" : "Disabling") + " systems.conduit.core.plugin: ";
        String postStateText = (enable ? "Enabled" : "Disabled") + " systems.conduit.core.plugin: ";
        return Commands.literal(state).then(Commands.argument("pluginName", StringArgumentType.word()).executes(c -> {
            String pluginName = StringArgumentType.getString(c, "pluginName");
            Optional<Plugin> plugin = Conduit.getPluginManager().getPlugin(pluginName);
            if (plugin.isPresent()) {
                pluginName = plugin.get().getMeta().name();
                c.getSource().sendSuccess(new TextComponent(preStateText + pluginName), false);
                if (enable) {
                    Conduit.getPluginManager().enable(plugin.get(), c.getSource().getEntity() == null);
                } else {
                    Conduit.getPluginManager().disable(plugin.get(), c.getSource().getEntity() == null);
                }
                c.getSource().sendSuccess(new TextComponent(postStateText + pluginName), false);
            } else {
                c.getSource().sendFailure(new TextComponent(pluginName + " is not a systems.conduit.core.plugin. Unable to " + state + "!"));
            }
            return 1;
        })).executes(c -> {
            c.getSource().sendFailure(new TextComponent("Please specify a systems.conduit.core.plugin to " + state + "!"));
            return 1;
        });
    }
}
