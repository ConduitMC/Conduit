package systems.conduit.main.mixins.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.events.types.PlayerEvents;

/**
 * @author Innectic
 * @since 11/27/2020
 */
@Mixin(value = PlayerList.class, remap = false)
public class PlayerListMixin {

    @Inject(method = "respawn", at = @At("TAIL"))
    public void respawn(ServerPlayer player, boolean b, CallbackInfoReturnable<ServerPlayer> cir) {
        PlayerEvents.RespawnEvent event = new PlayerEvents.RespawnEvent((systems.conduit.main.api.mixins.ServerPlayer) player);
        Conduit.getEventManager().dispatchEvent(event);
    }
}
