package systems.conduit.main.api.factories;

import lombok.Builder;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;

/**
 * Handles the creation of new levels.
 *
 * @author Innectic
 * @since 12/1/2019
 */
@Builder
public class LevelFactory {
    private String levelName;
    private GameType gameType;
    private boolean hardcore;
    private Difficulty difficulty;
    private boolean allowCommands;
    private GameRules gameRules;
    private DataPackConfig dataPackConfig;

    private long fixedTime;
    private boolean hasSkylight;
    private boolean hasCeiling;
    private boolean ultraWarm;
    private boolean natural;
    private boolean shrunk;
    private boolean createDragonFight;
    private boolean piglinSafe;
    private boolean bedWorks;
    private boolean respawnAnchorWorks;
    private boolean hasRaids;
    private int logicalHeight;
    private float ambientLight;

    private long seed;

    public void generate() {
        LevelSettings settings = new LevelSettings(levelName, gameType, hardcore, difficulty, allowCommands, gameRules, dataPackConfig);
        throw new UnsupportedOperationException("not implemented");
    }
}
