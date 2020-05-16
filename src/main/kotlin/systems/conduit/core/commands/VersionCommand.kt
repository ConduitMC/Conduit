package systems.conduit.core.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.SharedConstants
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.TextComponent
import systems.conduit.core.Conduit

class VersionCommand: BaseCommand() {

    override val command: LiteralArgumentBuilder<CommandSourceStack>?
        get() = Commands.literal("version").then(Commands.argument("pluginName", StringArgumentType.word()).executes { c: CommandContext<CommandSourceStack> ->
            var pluginName = StringArgumentType.getString(c, "pluginName")
            val plugin = Conduit.pluginManager.getPlugin(pluginName)
            if (plugin != null) {
                pluginName = plugin.meta.name()
                val version: String = plugin.meta.version()
                c.source.sendSuccess(TextComponent("$pluginName v$version"), false)
            } else {
                c.source.sendFailure(TextComponent("$pluginName is not a plugin!"))
            }
            1
        }).executes { c: CommandContext<CommandSourceStack> ->
            c.source.sendSuccess(TextComponent(Conduit.meta.name).append(" v").append(Conduit.meta.version), false)
            c.source.sendSuccess(TextComponent("Minecraft Server v" + SharedConstants.getCurrentVersion().name.split("/".toRegex()).toTypedArray()[0]), false)
            1
        }
}
