package systems.conduit.main.core.commands.conduit;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import javassist.tools.Callback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.plugin.Plugin;
import systems.conduit.main.core.utils.PermissionUtils;

import java.util.Optional;

public class PluginsCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> baseCommand() {
        return Commands.literal("plugins").requires(ctx -> PermissionUtils.checkPermissions(ctx, "conduit.admin", true, true)).executes(PluginsCommand::displayPluginList);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> reloadSubcommand() {
        return Commands.literal("reload").requires(ctx -> PermissionUtils.checkPermissions(ctx, "conduit.plugins.modify", true, true)).then(Commands.argument("pluginName", StringArgumentType.greedyString()).executes(c -> {
            String pluginName = StringArgumentType.getString(c, "pluginName");
            Optional<Plugin> plugin = Conduit.getPluginManager().getPlugin(pluginName);
            if (plugin.isPresent()) {
                if (!plugin.get().getMeta().reloadable()) {
                    // This plugin is not reloadable. Let the user know.
                    c.getSource().sendFailure(new TextComponent(pluginName + " is not reloadable."));
                    return 0;
                }
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

    public static int displayPluginList(CommandContext<CommandSourceStack> ctx) {
        Conduit.getPluginManager().getPlugins().stream().map(Plugin::toStringColored).reduce((a, b) -> a.concat(", ").concat(b))
                .ifPresent(s -> ctx.getSource().sendSuccess(new TextComponent("Plugins: " + s), false));
        return 1;
    }

    public static LiteralArgumentBuilder<CommandSourceStack> listPlugins() {
        return Commands.literal("list").executes(PluginsCommand::displayPluginList);
    }

    public static LiteralArgumentBuilder<CommandSourceStack> changeStateSubcommand(boolean enable) {
        String state = (enable ? "enable" : "disable");
        String preStateText = (enable ? "Enabling" : "Disabling") + " plugin: ";
        String postStateText = (enable ? "Enabled" : "Disabled") + " plugin: ";
        return Commands.literal(state).requires(ctx -> PermissionUtils.checkPermissions(ctx, "conduit.plugins.modify", true, true)).then(Commands.argument("pluginName", StringArgumentType.greedyString()).executes(c -> {
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
                c.getSource().sendFailure(new TextComponent(pluginName + " is not a plugin. Unable to " + state + "!"));
            }
            return 1;
        })).executes(c -> {
            c.getSource().sendFailure(new TextComponent("Please specify a plugin to " + state + "!"));
            return 1;
        });
    }
}
