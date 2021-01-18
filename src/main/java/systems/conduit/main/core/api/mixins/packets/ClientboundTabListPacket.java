package systems.conduit.main.core.api.mixins.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;

import java.io.IOException;

/**
 * @author Innectic
 * @since 1/6/2021
 */
public interface ClientboundTabListPacket {

    void read(FriendlyByteBuf buf) throws IOException;

    void write(FriendlyByteBuf buf) throws IOException;

    void handle(ClientGamePacketListener listener);

    default boolean isSkippable() {
        return false;
    }

    void setHeaderFooter(Component header, Component footer);
    Packet<ClientGamePacketListener> toPacket();
}
