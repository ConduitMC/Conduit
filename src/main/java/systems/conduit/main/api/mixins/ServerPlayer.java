package systems.conduit.main.api.mixins;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.GameType;
import systems.conduit.main.core.permissions.PermissionNode;

import java.util.List;

public interface ServerPlayer extends Player {

    void teleportTo(ServerLevel level, double x, double y, double z, float pitch, float yaw);
    int getContainerCounter();
    void onUpdateAbilities();
    ServerPlayerGameMode getGameMode();
    GameType getGameType();

    void kick(TextComponent kickMessage);
    void sendUpdatedAbilities();

    void addPermission(String permission);
    void removePermission(String permission);
    List<PermissionNode> getPermissionNodes();
    boolean hasPermission(String permission);

    net.minecraft.server.level.ServerPlayer down();
}
