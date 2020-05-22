package systems.conduit.core.commands

import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import javassist.tools.Callback
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.network.chat.TextComponent
import systems.conduit.core.Conduit

class PluginsCommand: BaseCommand() {

    override val command: LiteralArgumentBuilder<CommandSourceStack>?
        get() = baseCommand().then(reloadSubcommand()).then(changeStateSubcommand(true)).then(changeStateSubcommand(false))

    private fun baseCommand(): LiteralArgumentBuilder<CommandSourceStack> {
        return Commands.literal("plugins").executes { c: CommandContext<CommandSourceStack> ->
            val names = Conduit.pluginManager.plugins.map { it.toStringColored() }.reduce { a: String, b: String -> "$a,$b" }
            c.source.sendSuccess(TextComponent("Plugins: $names"), false)
            1
        }
    }

    private fun reloadSubcommand(): LiteralArgumentBuilder<CommandSourceStack> {
        return Commands.literal("reload").then(Commands.argument("pluginName", StringArgumentType.word()).executes { c: CommandContext<CommandSourceStack> ->
            var pluginName = StringArgumentType.getString(c, "pluginName")
            val plugin = Conduit.pluginManager.getPlugin(pluginName)
            if (plugin != null) {
                if (!plugin.meta.reloadable) {
                    // This plugin is not reloadable. Let the user know.
                    c.source.sendFailure(TextComponent("$pluginName is not reloadable."))
                    return@executes 0
                }
                pluginName = plugin.meta.name
                c.source.sendSuccess(TextComponent("Reloading plugin: $pluginName"), false)
                Conduit.pluginManager.reload(plugin, c.source.entity == null)
                c.source.sendSuccess(TextComponent("Reloaded plugin: $pluginName"), false)
            } else {
                c.source.sendFailure(TextComponent("$pluginName is not a plugin. Unable to reload!"))
            }
            1
        }).executes { c: CommandContext<CommandSourceStack> ->
            c.source.sendSuccess(TextComponent("Reloading all plugins..."), false)
            Conduit.pluginManager.reloadPlugins(c.source.entity == null, object: Callback("Reload callback") {
                override fun result(objects: Array<Any>) {
                    c.source.sendSuccess(TextComponent("Reloaded all plugins"), false)
                }
            })
            1
        }
    }

    private fun changeStateSubcommand(enable: Boolean): LiteralArgumentBuilder<CommandSourceStack> {
        val state = if (enable) "enable" else "disable"
        val preStateText = (if (enable) "Enabling" else "Disabling") + " plugin: "
        val postStateText = (if (enable) "Enabled" else "Disabled") + " plugin: "
        return Commands.literal(state).then(Commands.argument("pluginName", StringArgumentType.word()).executes { c: CommandContext<CommandSourceStack> ->
            var pluginName = StringArgumentType.getString(c, "pluginName")
            val plugin = Conduit.pluginManager.getPlugin(pluginName)
            if (plugin != null) {
                pluginName = plugin.meta.name
                c.source.sendSuccess(TextComponent(preStateText + pluginName), false)
                if (enable) {
                    Conduit.pluginManager.enable(plugin, c.source.entity == null)
                } else {
                    Conduit.pluginManager.disable(plugin, c.source.entity == null)
                }
                c.source.sendSuccess(TextComponent(postStateText + pluginName), false)
            } else {
                c.source.sendFailure(TextComponent("$pluginName is not a plugin. Unable to $state!"))
            }
            1
        }).executes { c: CommandContext<CommandSourceStack> ->
            c.source.sendFailure(TextComponent("Please specify a plugin to $state!"))
            1
        }
    }
}
