package systems.conduit.main.core.api.managers;

import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.api.mixins.ServerPlayer;

import java.util.Optional;

/**
 * @author Innectic
 * @since 1/3/2021
 */
public class ScoreboardManager {

    public Optional<Objective> getOrCreateObjective(String name) {
        return Conduit.getServer().map(s -> s.getScoreboard().getOrCreateObjective(name));
    }

    public boolean isExistingObjective(String name) {
        return Conduit.getServer().map(s -> s.getScoreboard().getObjective(name) == null).orElse(false);
    }

    public void deleteObjective(String name) {
        if (!isExistingObjective(name)) return;

        Conduit.getServer().ifPresent(s -> s.getScoreboard().removeObjective(s.getScoreboard().getObjective(name)));
    }

    public Optional<PlayerTeam> getTeam(String name) {
        return Conduit.getServer().map(s -> s.getScoreboard().getPlayerTeam(name));
    }

    public boolean teamExists(String name) {
        return getTeam(name).isPresent();
    }

    public Optional<PlayerTeam> getTeamForPlayer(ServerPlayer player) {
        return getTeamForPlayer(player.getGameProfile().getName());
    }

    public Optional<PlayerTeam> getTeamForPlayer(String player) {
        return Conduit.getServer().map(s -> s.getScoreboard().getPlayersTeam(player));
    }

    public void addPlayerToTeam(ServerPlayer player, String team) {
        addPlayerToTeam(player.getGameProfile().getName(), team);
    }

    public void addPlayerToTeam(String player, String name) {
        Optional<PlayerTeam> team = getTeam(name);
        if (!team.isPresent()) return;
        Conduit.getServer().ifPresent(s -> s.getScoreboard().addPlayerToTeam(player, team.get()));
    }

    public void removePlayerFromTeam(ServerPlayer player, String team) {
        removePlayerFromTeam(player.getGameProfile().getName(), team);
    }

    public void removePlayerFromTeam(String player, String name) {
        Optional<PlayerTeam> team = getTeam(name);
        if (!team.isPresent()) return;
        Conduit.getServer().ifPresent(s -> s.getScoreboard().removePlayerFromTeam(player, team.get()));
    }

    public void setScore(String player, String objective, int value) {
        getOrCreateObjective(objective).ifPresent(obj -> Conduit.getServer().ifPresent(s -> s.getScoreboard().getOrCreatePlayerScore(player, obj).setScore(value)));
    }

    public Optional<Integer> getScore(String player, String objective) {
        return getOrCreateObjective(objective).map(obj -> Conduit.getServer().map(s -> s.getScoreboard().getOrCreatePlayerScore(player, obj).getScore()).orElse(0));
    }
}
