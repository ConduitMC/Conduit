package systems.conduit.main.core.commands.conduit;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.SharedConstants;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.plugin.Plugin;
import systems.conduit.main.core.utils.PermissionUtils;

import java.util.Optional;

public class VersionCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> getCommand() {
        return Commands.literal("version").requires(ctx -> PermissionUtils.checkPermissions(ctx, "conduit.admin", false, true)).then(Commands.argument("pluginName", StringArgumentType.word()).executes(c -> {
            String pluginName = StringArgumentType.getString(c, "pluginName");
            Optional<Plugin> plugin = Conduit.getPluginManager().getPlugin(pluginName);
            if (plugin.isPresent()) {
                pluginName = plugin.get().getMeta().name();
                String version = plugin.get().getMeta().version();
                c.getSource().sendSuccess(new TextComponent(pluginName + " v" + version), false);
            } else c.getSource().sendFailure(new TextComponent(pluginName + " is not a plugin!"));
            return 1;
        })).executes(c -> {
            c.getSource().sendSuccess(new TextComponent("Conduit ").append(" v").append(Conduit.getVersion()), false);
            c.getSource().sendSuccess(new TextComponent("Minecraft Server v" + SharedConstants.getCurrentVersion().getName().split("/")[0]), false);
            return 1;
        });
    }
}
