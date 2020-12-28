package systems.conduit.main.api.mixins;

import net.minecraft.core.SectionPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.network.TextFilter;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import systems.conduit.main.core.permissions.PermissionNode;

import java.util.List;
import java.util.Optional;

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
    boolean isOp();

    net.minecraft.server.level.ServerPlayer down();

    void sendResourcePack(String url, String hash, boolean required);
    ChatVisiblity getChatVisibility();
    int getOpPermissionLevel();
    void resetLastActionTime();
    long getLastActionTime();

    ServerStatsCounter getStats();
    ServerRecipeBook getRecipeBook();
    PlayerAdvancements getAdvancements();

    void sendRemoveEntity(Entity entity);
    void cancelRemoveEntity(Entity entity);

    Entity getCamera();
    void conduit_setCamera(Entity camera);

    void untrackChunk(ChunkPos pos);
    SectionPos getLastSectionPos();
    void setLastSectionPos(SectionPos pos);

    Optional<TextFilter> getTextFilter();

    void setGameType(GameType type);

    void displayTitle(TextComponent title, TextComponent subtitle, int fadeIn, int stay, int fadeOut);
    void clearTitle();
    void displayActionBarTitle(TextComponent title, int fadeIn, int stay, int fadeOut);
}
