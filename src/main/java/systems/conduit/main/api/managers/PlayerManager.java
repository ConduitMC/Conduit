package systems.conduit.main.api.managers;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.dimension.DimensionType;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.MinecraftServer;
import systems.conduit.main.api.Player;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Innectic
 * @since 10/26/2019
 */
public class PlayerManager {

    /***
     * Find a player by name.
     *
     * @deprecated Use {@link #getPlayer(UUID)} instead.
     *
     * @param name The name of the player
     * @return the player, if they're online. Empty otherwise.
     */
    @Deprecated
    public Optional<Player> getPlayer(String name) {
        if (!Conduit.getServer().isPresent()) return Optional.empty();
        MinecraftServer server = Conduit.getServer().get();

        // Check all levels for the player
        for (Map.Entry<DimensionType, ServerLevel> entry : server.getLevels().entrySet()) {
            // Find all players who have matching names
            List<ServerPlayer> players = entry.getValue().getPlayers(s -> s.getName().getString().equals(name));
            if (players.size() == 0) return Optional.empty();

            // We'll assume that if there's any players in this list, that the first one should match the name. So, cast it to our type and return it.
            return Optional.of((Player) players.get(0));
        }
        return Optional.empty();
    }

    /**
     * Find a player by name in a given world.
     *
     * @deprecated Use {@link #getPlayer(UUID, ServerLevel)} instead.
     *
     * @param name the name of the player to find.
     * @param level the level the player should be in.
     * @return the player, if they're online and in the specified world. Empty otherwise.
     */
    @Deprecated
    public Optional<Player> getPlayerInWorld(String name, DimensionType level) {
        if (!Conduit.getServer().isPresent()) return Optional.empty();
        MinecraftServer server = Conduit.getServer().get();

        // Find all players who have the given name.
        List<ServerPlayer> players = server.getLevel(level).getPlayers(p -> p.getName().getString().equals(name));
        if (players.size() == 0) return Optional.empty();

        // We have a player that is most likely the one we want, so cast it to our and return it.
        return Optional.of((Player) players.get(0));
    }

    /**
     * Find a player by UUID.
     *
     * @param uuid the uuid of the player to find.
     * @return the player, if they're online. Empty otherwise.
     */
    public Optional<Player> getPlayer(UUID uuid) {
        if (!Conduit.getServer().isPresent()) return Optional.empty();
        MinecraftServer server = Conduit.getServer().get();

        // Check all levels for the player
        for (Map.Entry<DimensionType, ServerLevel> entry : server.getLevels().entrySet()) {
            // Find all players who have matching uuids
            List<ServerPlayer> players = entry.getValue().getPlayers(s -> s.getUUID().equals(uuid));
            if (players.size() == 0) return Optional.empty();

            // We'll assume that if there's any players in this list, that the first one should match the name. So, cast it to our type and return it.
            return Optional.of((Player) players.get(0));
        }
        return Optional.empty();
    }

    /**
     * Find a player by UUID, in a specified world.
     *
     * @param uuid the uuid of the player to find.
     * @param level the level that they should be in.
     * @return the player, if they're online and in the world. Empty otherwise.
     */
    public Optional<Player> getPlayer(UUID uuid, DimensionType level) {
        if (!Conduit.getServer().isPresent()) return Optional.empty();
        MinecraftServer server = Conduit.getServer().get();

        // Find all players who have the given name.
        List<ServerPlayer> players = server.getLevel(level).getPlayers(p -> p.getUUID().equals(uuid));
        if (players.size() == 0) return Optional.empty();

        // We have a player that is most likely the one we want, so cast it to our and return it.
        return Optional.of((Player) players.get(0));
    }
}
