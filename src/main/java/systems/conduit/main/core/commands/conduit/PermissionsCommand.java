package systems.conduit.main.core.commands.conduit;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import systems.conduit.main.api.mixins.ServerPlayer;
import systems.conduit.main.core.commands.argument.PlayerArgumentType;
import systems.conduit.main.core.permissions.PermissionNode;

import java.util.Optional;

/**
 * @author Innectic
 * @since 12/15/2020
 */
public class PermissionsCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> baseCommand() {
        return Commands.literal("permissions");
    }

    public static LiteralArgumentBuilder<CommandSourceStack> listPermissions() {
        return Commands.literal("list").then(Commands.argument("player", PlayerArgumentType.name()).executes(ctx -> {
            Optional<ServerPlayer> target = PlayerArgumentType.getPlayer(ctx, "player");

            if (!target.isPresent()) {
                ctx.getSource().sendFailure(new TextComponent("Could not find targeted player."));
                return 0;
            }

            ctx.getSource().getEntity().sendMessage(new TextComponent(target.get().getName()), Util.NIL_UUID);

            TextComponent permissionsListComponent = (TextComponent) new TextComponent("Permissions for ").append(target.get().getName()).append(": ");
            permissionsListComponent.append(target.get().getPermissionNodes().stream().map(PermissionNode::getPermission).reduce((a, b) -> a.concat(", ").concat(b)).orElse("No permissions!"));

            ctx.getSource().sendSuccess(permissionsListComponent, false);

            return 0;
        }));
    }
}
