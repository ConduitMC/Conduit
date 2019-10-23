package systems.conduit.main.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;
import systems.conduit.main.api.Player;
import systems.conduit.main.api.ServerPlayer;

public abstract class EventType {

    @AllArgsConstructor
    @Getter
    public static class BlockPlaceEvent extends Cancellable {
        private ServerPlayer player;
        private BlockState blockState;
        private InteractionHand hand;
        private Direction clickedFace;
    }

    @AllArgsConstructor
    @Getter
    public static class BlockInteractEvent extends Cancellable {
        private ServerPlayer player;
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
        private ServerPlayer player;
        private BlockState blockState;
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerJoinEvent extends EventType {
        private ServerPlayer player;
        @Setter private Component message;
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerLeaveEvent extends EventType {
        private ServerPlayer player;
    }

    @AllArgsConstructor
    @Getter
    public static class DamageMeta {
        private boolean isThorns;
        private boolean bypassArmor;
        private boolean bypassInvulnerability;
        private boolean bypassMagic;
        private boolean isCreativePlayer;
        private boolean isExplosion;
        private boolean isFire;
        private boolean isMagic;
        private boolean isProjectile;
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerDamageByEntityEvent extends Cancellable {
        private ServerPlayer damaged;
        private Entity damager;
        private float damage;
        private DamageMeta meta;
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerDamageByPlayerEvent extends Cancellable {
        private ServerPlayer damaged;
        private ServerPlayer damager;
        private float damage;
        private DamageMeta meta;
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerDamageByArrowEvent extends Cancellable {
        private ServerPlayer damaged;
        private Entity shooter;
        private AbstractArrow arrow;
        private float damage;
        private DamageMeta meta;
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerGameModeChangeEvent extends Cancellable {
        private ServerPlayer player;
        @Setter private GameType gamemode;
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerChatEvent extends Cancellable {
        private ServerPlayer player;
        @Setter private Component message;
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerCommandEvent extends Cancellable {
        private ServerPlayer player;
        @Setter private String message;
    }
}
