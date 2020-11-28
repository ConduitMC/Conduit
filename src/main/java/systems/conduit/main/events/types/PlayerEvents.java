package systems.conduit.main.events.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.api.Player;
import systems.conduit.main.events.Cancellable;

/*
 * @author Innectic
 * @since 10/21/2019
 */
public class PlayerEvents {

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.event.player.JoinMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class PlayerJoinEvent extends EventType {
        private Player player;
        @Setter private Component message;
    }

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.ServerGamePacketListenerMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class PlayerLeaveEvent extends EventType {
        private Player player;
        @Setter private Component message;
        private LeaveType type;
    }

    /**
     * Meta information provided by Minecraft.
     */
    @AllArgsConstructor
    @Getter
    public enum LeaveType {
        LEFT, // Manually left the game
        KICKED, // Got kicked by operator or plugin
        UNKNOWN // Unknown. Possibly should have a type for it if its common.
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
     * Mixin implementation: {@link systems.conduit.main.mixins.api.ServerPlayerMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class PlayerGameModeChangeEvent extends Cancellable {
        private Player player;
        @Setter private GameType gamemode;
    }

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.ServerGamePacketListenerMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class PlayerChatEvent extends Cancellable {
        private Player player;
        @Setter private Component message;
    }

    /**
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
    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.event.player.PlayerListMixin}
     */
    public static class RespawnEvent extends EventType {
        private ServerPlayer player;
    }

    @AllArgsConstructor
    @Getter
    public static class MoveEvent extends Cancellable {
        private Player player;
        private Vec3 to;
        private Vec3 from;
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

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.api.LivingEntityMixin#eat(Level, ItemStack, CallbackInfoReturnable)}
     */
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

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.api.ServerPlayerMixin#startSleeping(BlockPos, CallbackInfo)}
     */
    @AllArgsConstructor
    @Getter
    public static class EnterBedEvent extends Cancellable {
        private Player player;
        private BlockPos bed;
    }

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.api.ServerPlayerMixin#stopSleeping(CallbackInfo)}
     */
    @AllArgsConstructor
    @Getter
    public static class LeaveBedEvent extends EventType {  // @Future: Should this be cancellable?
        private Player player;
        private BlockPos bed;
    }

    @AllArgsConstructor
    @Getter
    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.api.ServerPlayerMixin#attack(Entity, CallbackInfo)}
     */
    public static class SpectateEvent extends Cancellable {
        private ServerPlayer player;
        private Entity spectating;
    }
}
