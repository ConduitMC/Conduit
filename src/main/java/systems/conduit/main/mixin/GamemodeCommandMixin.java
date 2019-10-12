package systems.conduit.main.mixin;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.commands.GameModeCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = GameModeCommand.class, remap = false)
public abstract class GamemodeCommandMixin {

    /**
     * Fixes gamemode command output using the wrong gamemode after changed by the event.
     * @author Clutch
     */
    @Overwrite
    private static void logGamemodeChange(CommandSourceStack var0, ServerPlayer var1, GameType var2) {
        Component var3 = new TranslatableComponent("gameMode." + var1.gameMode.getGameModeForPlayer().getName());
        if (var0.getEntity() == var1) {
            var0.sendSuccess(new TranslatableComponent("commands.gamemode.success.self", var3), true);
        } else {
            if (var0.getLevel().getGameRules().getBoolean(GameRules.RULE_SENDCOMMANDFEEDBACK)) {
                var1.sendMessage(new TranslatableComponent("gameMode.changed", var3));
            }
            var0.sendSuccess(new TranslatableComponent("commands.gamemode.success.other", var1.getDisplayName(), var3), true);
        }
    }
}
