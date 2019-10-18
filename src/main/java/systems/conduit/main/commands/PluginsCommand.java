package systems.conduit.main.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import javassist.tools.Callback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import systems.conduit.main.Conduit;
import systems.conduit.main.plugin.Plugin;

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
                pluginName = plugin.get().getMeta().name();
                c.getSource().sendSuccess(new TextComponent("Reloading plugin: " + pluginName), false);
                Conduit.getPluginManager().reload(plugin.get(), c.getSource().getEntity() == null);
                c.getSource().sendSuccess(new TextComponent("Reloaded plugin: " + pluginName), false);
            } else {
                c.getSource().sendFailure(new TextComponent(pluginName + " is not a plugin. Unable to reload!"));
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
        return Commands.literal(state).then(Commands.argument("pluginName", StringArgumentType.word()).executes(c -> {
            String pluginName = StringArgumentType.getString(c, "pluginName");
            Optional<Plugin> plugin = Conduit.getPluginManager().getPlugin(pluginName);
            if (plugin.isPresent()) {
                pluginName = plugin.get().getMeta().name();
                if (enable) {
                    c.getSource().sendSuccess(new TextComponent("Enabling plugin: " + pluginName), false);
                    Conduit.getPluginManager().enable(plugin.get(), c.getSource().getEntity() == null);
                    c.getSource().sendSuccess(new TextComponent("Enabled plugin: " + pluginName), false);
                } else {
                    c.getSource().sendSuccess(new TextComponent("Disabling plugin: " + pluginName), false);
                    Conduit.getPluginManager().disable(plugin.get(), c.getSource().getEntity() == null);
                    c.getSource().sendSuccess(new TextComponent("Disabled plugin: " + pluginName), false);
                }
            } else {
                c.getSource().sendFailure(new TextComponent(pluginName + " is not a plugin. Unable to " + state + "!"));
            }
            return 1;
        })).executes(c -> {
            c.getSource().sendFailure(new TextComponent("Please specify a plugin to " + state + "!"));
            return 1;
        });
    }
}
