package systems.conduit.main.mixin.api;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.ServerPlayer;
import systems.conduit.main.events.EventType;

@Mixin(value = net.minecraft.server.level.ServerPlayer.class, remap = false)
public abstract class ServerPlayerMixin implements ServerPlayer {

    @Shadow private int containerCounter;
    @Shadow protected abstract void nextContainerCounter();
    @Shadow public ServerGamePacketListenerImpl connection;

    @Override
    public void openContainer(SimpleContainer container, String title) {
        if (container.getContainerSize() % 9 != 0 || container.getContainerSize() <= 0 || container.getContainerSize() > 54) {
            throw new IllegalArgumentException("Container size needs to be divisible by 9. Max size 54!");
        }
        MenuType<ChestMenu> type = null;
        switch (container.getContainerSize() / 9) {
            case 1:
                type = MenuType.GENERIC_9x1;
                break;
            case 2:
                type = MenuType.GENERIC_9x2;
                break;
            case 3:
                type = MenuType.GENERIC_9x3;
                break;
            case 4:
                type = MenuType.GENERIC_9x4;
                break;
            case 5:
                type = MenuType.GENERIC_9x5;
                break;
            case 6:
                type = MenuType.GENERIC_9x6;
                break;
        }
        if (type == null) throw new NullPointerException("Menu type is somehow null!");

        this.nextContainerCounter();
        ChestMenu menu = new ChestMenu(type, this.containerCounter, ((Player) (Object) this).inventory, container, container.getContainerSize() / 9);
        this.connection.send(new ClientboundOpenScreenPacket(menu.containerId, menu.getType(), new TextComponent(title)));
        ((Player) (Object) this).containerMenu = menu;
        this.connection.send(new ClientboundContainerSetContentPacket(menu.containerId, ((Player) (Object) this).containerMenu.getItems()));
        //((Player) (Object) this).containerMenu.addSlotListener();
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
