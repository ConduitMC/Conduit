package systems.conduit.versions.v1165.mixins;

import net.minecraft.network.Connection;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.network.protocol.status.ServerboundStatusRequestPacket;
import net.minecraft.server.network.ServerStatusPacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.api.mixins.MinecraftServer;
import systems.conduit.main.core.events.types.ServerEvents;

import java.util.Optional;

/**
 * @author Innectic
 * @since 12/26/2020
 */
@Mixin(value = ServerStatusPacketListenerImpl.class, remap = false)
public class ServerStatusPacketListenerImplMixin {

    @Shadow @Final private Connection connection;

    /**
     * @author ConduitMC
     */
    @Overwrite
    public void handleStatusRequest(ServerboundStatusRequestPacket packet) {
        Optional<MinecraftServer> server = Conduit.getServer();
        if (!server.isPresent()) {
            Conduit.getLogger().error("INTERNAL ERROR: Could not get actively running minecraft server?!");
            return;
        }
        ServerEvents.ServerStatusRequestEvent event = ServerEvents.ServerStatusRequestEvent.of(server.get().getStatus());
        Conduit.getEventManager().dispatchEvent(event);

        ServerStatus status = new ServerStatus();
        status.setDescription(event.getMotd());
        status.setFavicon(event.getServerIcon());
        status.setPlayers(event.getOnlinePlayers());
        status.setVersion(event.getServerVersion());

        this.connection.send(new ClientboundStatusResponsePacket(status));
    }
}
