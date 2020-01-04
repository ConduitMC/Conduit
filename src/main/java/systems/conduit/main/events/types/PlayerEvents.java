package systems.conduit.main.events.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Location;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import systems.conduit.main.api.Player;
import systems.conduit.main.events.Cancellable;

/*
 * @author Innectic
 * @since 10/21/2019
 */
public class PlayerEvents {

    /**
     * Player join server event.
     * Mixin implementation: {@link systems.conduit.main.mixins.event.player.JoinMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class PlayerJoinEvent extends EventType {
        private Player player;
        @Setter private Component message;
    }

    /**
     * Player leave server event.
     * This is not currently implemented.
     */
    @AllArgsConstructor
    @Getter
    public static class PlayerLeaveEvent extends EventType {
        private Player player;
        @Setter private Component message;
    }

    /**
     * Meta information provided by Minecraft.
     */
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

    /**
     * Player damage by entity event.
     * Mixin implementation: {@link systems.conduit.main.mixins.event.player.DamageMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class PlayerDamageByEntityEvent extends Cancellable {
        private Player damaged;
        private Entity damager;
        private float damage;
        private DamageMeta meta;
    }

    /**
     * Player damage by another player event.
     * Mixin implementation: {@link systems.conduit.main.mixins.event.player.DamageMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class PlayerDamageByPlayerEvent extends Cancellable {
        private Player damaged;
        private Player damager;
        private float damage;
        private DamageMeta meta;
    }

    /**
     * Player damage by arrow event. This can be either another player, skeleton, etc.
     * Mixin implementation: {@link systems.conduit.main.mixins.event.player.DamageMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class PlayerDamageByArrowEvent extends Cancellable {
        private Player damaged;
        private Entity shooter;
        private AbstractArrow arrow;
        private float damage;
        private DamageMeta meta;
    }

    /**
     * Player game mode change event.
     * Mixin implementation: {@link systems.conduit.main.mixins.api.ServerPlayerMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class PlayerGameModeChangeEvent extends Cancellable {
        private Player player;
        @Setter private GameType gamemode;
    }

    /**
     * Player chat event. No commands included.
     * Mixin implementation: {@link systems.conduit.main.mixins.ServerGamePacketListenerMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class PlayerChatEvent extends Cancellable {
        private Player player;
        @Setter private Component message;
    }

    /**
     * Player send command event.
     * Mixin implementation: {@link systems.conduit.main.mixins.ServerGamePacketListenerMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class PlayerCommandEvent extends Cancellable {
        private Player player;
        @Setter private String message;
    }

    @AllArgsConstructor
    @Getter
    public static class RespawnEvent extends EventType {
        private Player player;
    }

    @AllArgsConstructor
    @Getter
    public static class MoveEvent extends Cancellable {
        private Player player;
        private Location to;
        private Location from;
    }

    @AllArgsConstructor
    @Getter
    public static class LevelSwitchEvent extends Cancellable {
        private Player player;
        private Level to;
        private Level from;
    }

    @AllArgsConstructor
    @Getter
    public static class KickEvent extends EventType {
        private Player player;
        private String reason;
    }

    @AllArgsConstructor
    @Getter
    public static class ConsumeEvent extends EventType {
        private Player player;
        private ItemStack consumed;
    }

    @AllArgsConstructor
    @Getter
    public static class FishEvent extends Cancellable {
        private Player player;
    }

    @AllArgsConstructor
    @Getter
    public static class EnterBedEvent extends Cancellable {
        private Player player;
        private BlockPos bed;
    }

    @AllArgsConstructor
    @Getter
    public static class LeaveBedEvent extends Cancellable {
        private Player player;
        private BlockPos bed;
    }

    @AllArgsConstructor
    @Getter
    public static class SpectateEvent extends Cancellable {
        private Player player;
        private Entity spectating;
    }
}
