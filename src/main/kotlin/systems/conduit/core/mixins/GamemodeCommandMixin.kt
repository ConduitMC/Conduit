@file:JvmName("GamemodeCommandMixin")
@file:Mixin(value = [GameModeCommand::class], remap = false)
package systems.conduit.core.mixins

import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.server.commands.GameModeCommand
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.GameType
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Overwrite

/**
 * Fixes gamemode command output using the wrong gamemode after changed by the event.
 * @author ConduitMC
 */
@Overwrite
private fun logGamemodeChange(source: CommandSourceStack, player: ServerPlayer, gameType: GameType) {
    val var3: Component = TranslatableComponent("gameMode." + player.gameMode.gameModeForPlayer.getName())
    if (source.entity === player) {
        source.sendSuccess(TranslatableComponent("systems.conduit.core.commands.gamemode.success.self", var3), true)
    } else {
        if (source.level.gameRules.getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK)) {
            player.sendMessage(TranslatableComponent("gameMode.changed", var3))
        }
        source.sendSuccess(TranslatableComponent("systems.conduit.core.commands.gamemode.success.other", player.displayName, var3), true)
    }
}
