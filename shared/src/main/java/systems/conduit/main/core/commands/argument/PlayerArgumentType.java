package systems.conduit.main.core.commands.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.api.mixins.ServerPlayer;

import java.util.Optional;

/**
 * @author Innectic
 * @since 12/15/2020
 */
public class PlayerArgumentType implements ArgumentType<ServerPlayer> {

    @Override
    public ServerPlayer parse(StringReader reader) {
        String name = reader.getRemaining();
        reader.setCursor(reader.getTotalLength());

        return Conduit.getPlayerManager().getPlayer(name).orElse(null);
    }

    public static PlayerArgumentType name() {
        return new PlayerArgumentType();
    }

    public static Optional<ServerPlayer> getPlayer(CommandContext<CommandSourceStack> context, String name) {
        try {
            return Optional.of(context.getArgument(name, ServerPlayer.class));
        } catch (Exception ignored) {}
        return Optional.empty();
    }
}
