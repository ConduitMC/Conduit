package systems.conduit.main.core.api.mixins;

import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.network.TextFilter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
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

    Entity conduit_getCamera();
    void conduit_setCamera(Entity camera);

    void untrackChunk(ChunkPos pos);
    SectionPos getLastSectionPos();
    void setLastSectionPos(SectionPos pos);

    Optional<TextFilter> conduit_getTextFilter();

    void setGameType(GameType type);

    void displayTitle(TextComponent title, TextComponent subtitle, int fadeIn, int stay, int fadeOut);
    void clearTitle();
    void displayActionBarTitle(TextComponent title, int fadeIn, int stay, int fadeOut);

    void playSoundAt(SoundEvent sound, SoundSource source, double x, double y, double z, float volume, float pitch);
    void showParticle(ParticleOptions particle, boolean overrideLimiter, double x, double y, double z, float xDist, float yDist, float zDist, float maxSpeed, int count);

    void addToTeam(String team);
    void removeFromTeam(String team);
    void setObjectiveScore(String objective, int score);
    void addObjectiveScore(String objective, int score);
    void removeObjectiveScore(String objective, int score);
}
