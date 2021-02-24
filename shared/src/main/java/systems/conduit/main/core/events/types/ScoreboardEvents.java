package systems.conduit.main.core.events.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Score;

import java.util.Optional;

/**
 * @author Innectic
 * @since 1/3/2021
 */
public class ScoreboardEvents {

    @Getter
    @AllArgsConstructor
    public static class ScoreChangedEvent extends EventType {
        private ServerScoreboard scoreboard;
        private Score score;
    }

    @Getter
    @AllArgsConstructor
    public static class PlayerRemovedFromScoreboardEvent extends EventType {
        private ServerScoreboard scoreboard;
        private String player;
    }

    @Getter
    @AllArgsConstructor
    public static class PlayerScoreRemovedEvent extends EventType {
        private ServerScoreboard scoreboard;
        private Objective objective;
        private String player;
    }

    @Getter
    @AllArgsConstructor
    public static class ScoreboardSetDisplayObjectiveEvent extends EventType {
        private ServerScoreboard scoreboard;
        private int slot;
        private Optional<Objective> objective;
    }

    @Getter
    @AllArgsConstructor
    public static class AddPlayerToTeamEvent extends EventType {
        private ServerScoreboard scoreboard;
        private String player;
        private PlayerTeam team;
    }

    @Getter
    @AllArgsConstructor
    public static class RemovePlayerFromTeamEvent extends EventType {
        private ServerScoreboard scoreboard;
        private String player;
        private PlayerTeam team;
    }

    @Getter
    @AllArgsConstructor
    public static class ObjectiveAddedEvent extends EventType {
        private ServerScoreboard scoreboard;
        private Objective objective;
    }

    @Getter
    @AllArgsConstructor
    public static class ObjectiveChangedEvent extends EventType {
        private ServerScoreboard scoreboard;
        private Objective objective;
    }

    @Getter
    @AllArgsConstructor
    public static class ObjectiveRemovedEvent extends EventType {
        private ServerScoreboard scoreboard;
        private Objective objective;
    }

    @Getter
    @AllArgsConstructor
    public static class TeamAddedEvent extends EventType {
        private ServerScoreboard scoreboard;
        private PlayerTeam team;
    }

    @Getter
    @AllArgsConstructor
    public static class TeamChangedEvent extends EventType {
        private ServerScoreboard scoreboard;
        private PlayerTeam team;
    }

    @Getter
    @AllArgsConstructor
    public static class TeamRemovedEvent extends EventType {
        private ServerScoreboard scoreboard;
        private PlayerTeam team;
    }
}
