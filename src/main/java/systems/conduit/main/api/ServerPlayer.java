package systems.conduit.main.api;

public interface ServerPlayer extends Player {

    void teleportTo(ServerLevel level, double x, double y, double z, float pitch, float yaw);
    int getContainerCounter();
    void onUpdateAbilities();

    net.minecraft.server.level.ServerPlayer down();
}
