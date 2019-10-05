package me.ifydev.serverwrapper.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Innectic
 * @since 10/2/2019
 */
@Getter
public abstract class EventType {

    public abstract int getId();

    @AllArgsConstructor
    @Getter
    public static class BlockInteractEvent extends EventType {
        private Player player;
        private BlockState blockState;
        private InteractionHand hand;

        @Override
        public int getId() {
            return 0;
        }
    }

    @AllArgsConstructor
    @Getter
    public static class BlockBreakEvent extends EventType {
        private Player player;
        private BlockState blockState;

        @Override
        public int getId() {
            return 1;
        }
    }

    @AllArgsConstructor
    @Getter
    public static class BlockPlaceEvent extends EventType {
        private Player player;
        private BlockState blockState;

        @Override
        public int getId() {
            return 2;
        }
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerJoinEvent extends EventType {
        private Player player;

        @Override
        public int getId() {
            return 3;
        }
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerLeaveEvent extends EventType {
        private Player player;

        @Override
        public int getId() {
            return 4;
        }
    }
}
