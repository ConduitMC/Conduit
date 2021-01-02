package systems.conduit.main.core.events.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ProgressListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.api.mixins.*;
import systems.conduit.main.core.events.Cancellable;
import systems.conduit.main.mixins.item.ItemStackMixin;
import systems.conduit.main.mixins.level.ServerLevelDataMixin;
import systems.conduit.main.mixins.level.ServerLevelMixin;

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

    /**
     * Implemented in: {@link ItemStackMixin#useOn(UseOnContext, CallbackInfoReturnable)}
     */
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

    /**
     * Implemented in: {@link systems.conduit.main.mixins.player.ServerPlayerGameModeMixin#destroyBlockInitial(BlockPos, CallbackInfoReturnable)}
     */
    @AllArgsConstructor
    @Getter
    public static class BlockBreakEvent extends Cancellable {
        private Player player;
        private BlockState blockState;
        private BlockPos position;
    }

    /**
     * Implemented in {@link ServerLevelMixin#onLevelSave(ProgressListener, boolean, boolean, CallbackInfo)}
     */
    @AllArgsConstructor
    @Getter
    public static class WorldSaveEvent extends EventType {
        private ServerLevel level;
    }

    @AllArgsConstructor
    @Getter
    public static class ChunkLoadEvent extends Cancellable {
        private Level level;
        private LevelChunk chunk;
    }

    @AllArgsConstructor
    @Getter
    public static class ChunkUnloadEvent extends Cancellable {
        private Level level;
        private LevelChunk chunk;
    }

    @AllArgsConstructor
    @Getter
    public static class PortalCreateEvent extends EventType {
        private BlockPos blockPos;
        private Direction.Axis axis;
    }

    /**
     * Implemented in: {@link ServerLevelDataMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class ThunderChangeStateEvent extends Cancellable {
        private boolean isThundering;
        private Optional<Integer> time;
    }

    /**
     * Implemented in: {@link ServerLevelDataMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class RainChangeStateEvent extends Cancellable {
        private int rainTime;
    }

    /**
     * Implemented in: {@link ServerLevelDataMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class LightningStrikeEvent extends Cancellable {
        private Vec3 location;
    }

    /**
     * Implemented in {@link ServerLevelDataMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class WeatherClearEvent extends Cancellable {
        private int duration;
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
        private BlockState block;
        private ServerLevel level;
    }

    @AllArgsConstructor
    @Getter
    public static class BlockIgniteEvent extends Cancellable {
        private Block block;
        // TODO: Ignite source
    }

    @AllArgsConstructor
    @Getter
    public static class SpawnChangeEvent extends EventType {
        private BlockPos position;
        private ServerLevel level;
    }

    @AllArgsConstructor
    @Getter
    public static class ChestOpenEvent extends Cancellable {
        private BlockPos position;
        private Level level;
        private BlockState state;
        private ServerPlayer player;
        private InteractionHand hand;
    }

    @AllArgsConstructor
    @Getter
    public static class PiglinAngerEvent extends Cancellable {

        private interface AngerSource {}

        @Getter
        @AllArgsConstructor
        public static class ChestSource implements AngerSource {
            private BlockState state;
        }

        private ServerPlayer player;
        private Level level;
        private AngerSource angerSource;
    }

    @AllArgsConstructor
    @Getter
    public static class EnderChestOpenEvent extends Cancellable {
        private BlockPos position;
        private Level level;
        private BlockState state;
        private ServerPlayer player;
        private InteractionHand hand;
    }

    @AllArgsConstructor
    @Getter
    public static class ItemFrameDropItemEvent extends Cancellable {
        private BlockPos position;
        private ItemStack item;
    }

    @AllArgsConstructor
    @Getter
    public static class ItemFrameDisplayItemEvent extends Cancellable {
        private BlockPos position;
        private ItemStack item;
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerRingBellEvent extends Cancellable {
        private Level level;
        private BlockState state;
        private Player player;
        private InteractionHand hand;
        private BlockPos pos;
    }

    @AllArgsConstructor
    @Getter
    public static class ProjectileRingBellEvent extends Cancellable {
        private Level level;
        private BlockState state;
        private Projectile projectile;
    }

    @AllArgsConstructor
    @Getter
    public static class ShulkerBoxOpenEvent extends Cancellable {
        private BlockPos position;
        private Level level;
        private BlockState state;
        private ServerPlayer player;
        private InteractionHand hand;
    }

    @AllArgsConstructor
    @Getter
    public static class NoteBlockPlayNoteEvent extends Cancellable {
        private Level level;
        private BlockPos pos;
    }

    @AllArgsConstructor
    @Getter
    public static class NoteBlockTuneEvent extends Cancellable {
        private BlockState blockState;
        private Level level;
        private BlockPos blockPos;
        private Player player;
        private InteractionHand interactionHand;
    }

    @AllArgsConstructor
    @Getter
    public static class JukeboxChangeRecordEvent extends Cancellable {
        private BlockPos position;
        private BlockState jukebox;
        private ItemStack record;
    }

    @AllArgsConstructor
    @Getter
    public static class JukeboxDropRecordEvent extends Cancellable {
        private BlockPos position;
        private JukeboxBlockEntity jukebox;
        private ItemStack record;
        private Level level;
    }

    @AllArgsConstructor
    @Getter
    public static class SpawnerSpawnEvent extends Cancellable {
        private BaseSpawner spawner;
        private ServerLevel level;
        private Entity spawned;
    }

    @AllArgsConstructor
    @Getter
    public static class LecternPlaceBookEvent extends Cancellable {
        private BlockPos blockPos;
        private Level level;
        private ItemStack book;
        private ServerPlayer player;
    }
}
