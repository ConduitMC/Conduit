package systems.conduit.main.core.utils;

import net.minecraft.commands.CommandSourceStack;
import systems.conduit.main.api.mixins.ServerPlayer;

/**
 * @author Innectic
 * @since 12/15/2020
 */
public class PermissionUtils {

    public static boolean checkPermissions(CommandSourceStack ctx, String permission, boolean orOp, boolean consoleAllowed) {
        if (ctx.getEntity() == null) {
            // Probably a console sender
            return consoleAllowed;
        } else {
            // Player sender. Make sure they're a player, and then check permissions /op
            if (!(ctx.getEntity() instanceof ServerPlayer)) return false;
            ServerPlayer player = (ServerPlayer) ctx.getEntity();

            if (player.hasPermission(permission)) return true;
            return orOp && player.isOp();
        }
    }
}
