package systems.conduit.main.mixins.api;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.ServerPlayer;
import systems.conduit.main.events.EventType;
import systems.conduit.main.inventory.ChestContainer;

@Mixin(value = net.minecraft.server.level.ServerPlayer.class, remap = false)
public abstract class ServerPlayerMixin implements ServerPlayer {

    @Shadow private int containerCounter;
    @Shadow protected abstract void nextContainerCounter();
    @Shadow public ServerGamePacketListenerImpl connection;


    @Override
    public void openContainer(ChestContainer container) {
        this.closeOpenedContainer();
        this.nextContainerCounter();
        AbstractContainerMenu menu = new ChestMenu(container.getType(), this.containerCounter, ((Player) (Object) this).inventory, container, container.getContainerSize() / 9);
        menu.addSlotListener((net.minecraft.server.level.ServerPlayer) (Object) this);
        this.connection.send(new ClientboundOpenScreenPacket(menu.containerId, menu.getType(), new TextComponent(container.getTitle())));
        ((Player) (Object) this).containerMenu = menu;
        this.connection.send(new ClientboundContainerSetContentPacket(menu.containerId, ((Player) (Object) this).containerMenu.getItems()));
    }

    @ModifyVariable(method = "setGameMode", at = @At("HEAD"))
    private GameType updateGameMode(GameType gameType) {
        // TODO: Allow this event to be cancelled
        //
        // We'll need to find a different way to hook into gamemode changes.

        EventType.PlayerGameModeChangeEvent event = new EventType.PlayerGameModeChangeEvent(this, gameType);
        Conduit.getEventManager().dispatchEvent(event);
        return event.getGamemode();
    }
}
