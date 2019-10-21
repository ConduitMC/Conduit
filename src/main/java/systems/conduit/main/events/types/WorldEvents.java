package systems.conduit.main.events.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import systems.conduit.main.api.Player;
import systems.conduit.main.events.Cancellable;

/**
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
}
