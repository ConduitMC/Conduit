package systems.conduit.main.events.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import systems.conduit.main.api.Level;
import systems.conduit.main.api.Player;
import systems.conduit.main.events.Cancellable;

import java.util.Optional;

/*
 * @author Innectic
 * @since 10/21/2019
 */
public class WorldEvents {

    @AllArgsConstructor
    @Getter
    public static class BlockPlaceEvent extends Cancellable {
        private Player player;
        private BlockState blockState;
        private InteractionHand hand;
        private Direction clickedFace;
    }

    @AllArgsConstructor
    @Getter
    public static class BlockInteractEvent extends Cancellable {
        private Player player;
        private BlockState blockState;
        private InteractionHand hand;
        private ItemStack itemInHand;
        private Direction clickedFace;
        private boolean sneaking;
        private boolean inside;
    }

    @AllArgsConstructor
    @Getter
    public static class BlockBreakEvent extends Cancellable {
        private Player player;
        private BlockState blockState;
    }

    @AllArgsConstructor
    @Getter
    public static class WorldSaveEvent extends EventType {
        private Optional<ServerLevel> level;
    }

    @AllArgsConstructor
    @Getter
    public static class ChunkLoadEvent extends Cancellable {
        private Level level;
        private ChunkPos chunk;
    }

    @AllArgsConstructor
    @Getter
    public static class ChunkUnloadEvent extends Cancellable {
        private Level level;
        private ChunkPos chunk;
    }

    @AllArgsConstructor
    @Getter
    public static class PortalCreateEvent extends EventType {
        // TODO: Whatever goes in here
    }

    @AllArgsConstructor
    @Getter
    public static class WeatherChangeEvent extends Cancellable {
        // TODO: Whatever goes in here
    }

    @AllArgsConstructor
    @Getter
    public static class LightningStrikeEvent extends Cancellable {
        private Vec3 location;
    }

    @AllArgsConstructor
    @Getter
    public static class ThunderStatusChangeEvent extends Cancellable {
        private boolean isThundering;
    }

    @AllArgsConstructor
    @Getter
    public static class GrowEvent extends Cancellable {
        private Block block;
    }

    @AllArgsConstructor
    @Getter
    public static class BlockBurnEvent extends Cancellable {
        private Block burned;
    }

    @AllArgsConstructor
    @Getter
    public static class LeafDecayEvent extends Cancellable {
        private Block block;
    }

    @AllArgsConstructor
    @Getter
    public static class BlockIgniteEvent extends Cancellable {
        private Block block;
        // TODO: Ignite source
    }
}
