package systems.conduit.main.core.api.factories;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Getter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.entity.npc.CatSpawner;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import net.minecraft.world.level.levelgen.PhantomSpawner;

import java.util.List;
import java.util.Random;

/**
 * Handles the creation of new levels.
 *
 * @author Innectic
 * @since 12/1/2019
 */
@Builder
@Getter
public class LevelDataFactory {
    public static final List<CustomSpawner> DEFAULT_CUSTOM_SPAWNERS = ImmutableList.of(new PhantomSpawner(), new PatrolSpawner(), new CatSpawner(), new VillageSiege());

    private String levelName;

    @Builder.Default private GameType gameType = GameType.SURVIVAL;
    @Builder.Default private Difficulty difficulty = Difficulty.NORMAL;

    @Builder.Default private GameRules gameRules = new GameRules();
    @Builder.Default private DataPackConfig dataPackConfig = DataPackConfig.DEFAULT;
    @Builder.Default private ResourceKey<DimensionType> dimensionType = DimensionType.OVERWORLD_LOCATION;

    @Builder.Default private int logicalHeight = 256;
    @Builder.Default private float ambientLight = 7;
    @Builder.Default private long seed = new Random().nextLong();
    private long fixedTime;

    @Builder.Default private boolean hardcore = false;
    @Builder.Default private boolean allowCommands = true;
    @Builder.Default private boolean hasSkylight = true;
    @Builder.Default private boolean hasCeiling = false;
    @Builder.Default private boolean ultraWarm = false;
    @Builder.Default private boolean natural = true;
    @Builder.Default private boolean shrunk = false;
    @Builder.Default private boolean createDragonFight = false;
    @Builder.Default private boolean piglinSafe = true;
    @Builder.Default private boolean bedWorks = true;
    @Builder.Default private boolean respawnAnchorWorks = false;
    @Builder.Default private boolean hasRaids = true;
    @Builder.Default private boolean generateFeatures = true;
    @Builder.Default private boolean generateBonusChest = false;
    @Builder.Default private boolean largeBiomes = false;

    @Builder.Default private List<CustomSpawner> customSpawners = DEFAULT_CUSTOM_SPAWNERS;
}
