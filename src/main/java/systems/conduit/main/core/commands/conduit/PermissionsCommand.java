package systems.conduit.main.core.commands.conduit;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import systems.conduit.main.api.mixins.ServerPlayer;
import systems.conduit.main.core.commands.argument.PlayerArgumentType;
import systems.conduit.main.core.permissions.PermissionNode;
import systems.conduit.main.core.utils.PermissionUtils;

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
        return Commands.literal("list").requires(ctx -> PermissionUtils.checkPermissions(ctx, "conduit.permissions.list", false, true)).then(Commands.argument("player", PlayerArgumentType.name()).executes(ctx -> {
            Optional<ServerPlayer> target = PlayerArgumentType.getPlayer(ctx, "player");

            if (!target.isPresent()) {
                // TODO Check if sender is a player
                ctx.getSource().sendFailure(new TextComponent("Could not find targeted player."));
                return 0;
            }

            TextComponent permissionsListComponent = (TextComponent) new TextComponent("Permissions for ").append(target.get().getName()).append(": ");
            permissionsListComponent.append(target.get().getPermissionNodes().stream().map(PermissionNode::getPermission).reduce((a, b) -> a.concat(", ").concat(b)).orElse("No permissions!"));

            ctx.getSource().sendSuccess(permissionsListComponent, false);

            return 1;
        }));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> addPermission() {
        return Commands.literal("add").requires(ctx -> PermissionUtils.checkPermissions(ctx, "conduit.permissions.add", false, true)).then(Commands.argument("permission", StringArgumentType.word()).then(Commands.argument("player", PlayerArgumentType.name()).executes(ctx -> {
            Optional<ServerPlayer> target = PlayerArgumentType.getPlayer(ctx, "player");
            if (!target.isPresent()) {
                // TODO: check if sender is a player
                ctx.getSource().sendFailure(new TextComponent("Could not find targeted player"));
                return 0;
            }

            target.get().addPermission(StringArgumentType.getString(ctx, "permission"));
            ctx.getSource().sendSuccess(new TextComponent("Permission has been added!"), false);

            return 1;
        })));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> removePermission() {
        return Commands.literal("remove").requires(ctx -> PermissionUtils.checkPermissions(ctx, "conduit.permissions.remove", false, true)).then(Commands.argument("permission", StringArgumentType.word()).then(Commands.argument("player", PlayerArgumentType.name()).executes(ctx -> {
            Optional<ServerPlayer> target = PlayerArgumentType.getPlayer(ctx, "player");
            if (!target.isPresent()) {
                // TODO: check if sender is a player
                ctx.getSource().sendFailure(new TextComponent("Could not find targeted player"));
                return 0;
            }

            target.get().removePermission(StringArgumentType.getString(ctx, "permission"));
            ctx.getSource().sendSuccess(new TextComponent("Permission has been removed!"), false);

            return 1;
        })));
    }
}
