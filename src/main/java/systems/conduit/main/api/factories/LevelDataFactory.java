package systems.conduit.main.api.factories;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
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

    private GameType gameType;
    private Difficulty difficulty;

    @Setter private GameRules gameRules;
    @Setter private DataPackConfig dataPackConfig;
    private ResourceKey<DimensionType> dimensionType;

    private int logicalHeight;
    private float ambientLight;
    private long seed;
    private long fixedTime;

    private boolean hardcore;
    private boolean allowCommands;
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
    private boolean generateFeatures;
    private boolean generateBonusChest;
    private boolean largeBiomes;

    @Setter private List<CustomSpawner> customSpawners;
}
