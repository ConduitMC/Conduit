package systems.conduit.versions.v1165.mixins.server.packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;

/**
 * @author Innectic
 * @since 1/6/2021
 */
@Mixin(value = ClientboundTabListPacket.class, remap = false)
public abstract class ClientboundTabListPacketMixin implements systems.conduit.main.core.api.mixins.packets.ClientboundTabListPacket {

    @Shadow public abstract void read(FriendlyByteBuf friendlyByteBuf) throws IOException;
    @Shadow public abstract void write(FriendlyByteBuf friendlyByteBuf) throws IOException;
    @Shadow public abstract void handle(ClientGamePacketListener clientGamePacketListener);

    @Shadow private Component header;
    @Shadow private Component footer;

    @Override
    public void setHeaderFooter(Component header, Component footer) {
        this.header = header;
        this.footer = footer;
    }

    @Override
    public Packet<ClientGamePacketListener> toPacket() {
        return (Packet<ClientGamePacketListener>) ((Object) this);
    }
}
