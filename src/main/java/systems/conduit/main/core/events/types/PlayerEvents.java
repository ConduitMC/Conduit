package systems.conduit.main.core.events.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.api.mixins.LivingEntity;
import systems.conduit.main.api.mixins.Player;
import systems.conduit.main.api.mixins.ServerPlayer;
import systems.conduit.main.core.events.Cancellable;
import systems.conduit.main.mixins.server.ServerGamePacketListenerMixin;

import java.util.Collection;
import java.util.Optional;

/*
 * @author Innectic
 * @since 10/21/2019
 */
public class PlayerEvents {

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.player.JoinMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class PlayerJoinEvent extends EventType {
        private ServerPlayer player;
        @Setter private Component message;
    }

    /**
     * Mixin implementation: {@link ServerGamePacketListenerMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class PlayerLeaveEvent extends EventType {
        private ServerPlayer player;
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
     * Mixin implementation: {@link systems.conduit.main.mixins.player.DamageMixin}
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
     * Mixin implementation: {@link systems.conduit.main.mixins.player.DamageMixin}
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
     * Mixin implementation: {@link systems.conduit.main.mixins.player.DamageMixin}
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
     * Mixin implementation: {@link systems.conduit.main.mixins.player.ServerPlayerMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class PlayerGameModeChangeEvent extends Cancellable {
        private Player player;
        @Setter private GameType gamemode;
    }

    /**
     * Mixin implementation: {@link ServerGamePacketListenerMixin}
     */
    @AllArgsConstructor
    @Getter
    public static class PlayerChatEvent extends Cancellable {
        private Player player;
        @Setter private Component message;
    }

    /**
     * Mixin implementation: {@link ServerGamePacketListenerMixin}
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
     * Mixin implementation: {@link systems.conduit.main.mixins.player.PlayerListMixin}
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
        private Level current;
        private Level destination;
    }

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.server.command.KickCommandMixin#kickPlayers(CommandSourceStack, Collection, Component, CallbackInfoReturnable)}
     */
    @AllArgsConstructor
    @Getter
    public static class KickEvent extends Cancellable {
        private ServerPlayer player;
        private Component reason;
    }

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.entity.LivingEntityMixin#eat(Level, ItemStack, CallbackInfoReturnable)}
     */
    @AllArgsConstructor
    @Getter
    public static class ConsumeEvent extends EventType {
        private Player player;
        private ItemStack consumed;
    }

    @AllArgsConstructor
    @Getter
    public static class CaughtFishEvent extends Cancellable {
        private Player player;
        private ItemEntity fish;
    }

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.player.ServerPlayerMixin#startSleeping(BlockPos, CallbackInfo)}
     */
    @AllArgsConstructor
    @Getter
    public static class EnterBedEvent extends Cancellable {
        private Player player;
        private BlockPos bed;
    }

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.player.ServerPlayerMixin#stopSleeping(CallbackInfo)}
     */
    @AllArgsConstructor
    @Getter
    public static class LeaveBedEvent extends EventType {  // @Future: Should this be cancellable?
        private Player player;
        private BlockPos bed;
    }

    /**
     * Mixin implementation: {@link systems.conduit.main.mixins.player.ServerPlayerMixin#attack(Entity, CallbackInfo)}
     */
    @AllArgsConstructor
    @Getter
    public static class SpectateEvent extends Cancellable {
        private ServerPlayer player;
        private Entity spectating;
    }

    @AllArgsConstructor
    @Getter
    public static class InventoryClickEvent extends Cancellable {
        private ServerPlayer player;
        private AbstractContainerMenu container;
        private ClickType clickType;
        private int slotId;
    }

    @AllArgsConstructor
    @Getter
    public static class InventoryCloseEvent extends Cancellable {
        private ServerPlayer player;
        private AbstractContainerMenu container;
    }

    @AllArgsConstructor
    @Getter
    public static class InventoryOpenEvent extends Cancellable {
        private ServerPlayer player;
        private AbstractContainerMenu menu;
    }

    @AllArgsConstructor
    @Getter
    public static class DeathEvent extends EventType {
        private ServerPlayer player;
        private Optional<LivingEntity> killer;
        private DamageSource source;
    }

    @AllArgsConstructor
    @Getter
    public static class DropItemEvent extends Cancellable {
        private ServerPlayer player;
        private ItemStack item;
    }

    @AllArgsConstructor
    @Getter
    public static class EntityClickEvent extends Cancellable {
        private ServerPlayer player;
        private ItemStack itemInHand;
        private InteractionHand hand;
        private systems.conduit.main.api.mixins.Entity entity;
    }

    @AllArgsConstructor
    @Getter
    public static class MainHandChangeEvent extends EventType {
        private ServerPlayer player;
        private HumanoidArm arm;
    }

    @AllArgsConstructor
    @Getter
    public static class ItemInteractEvent extends Cancellable {
        private ServerPlayer player;
        private InteractionHand hand;
        private ItemStack itemInHand;
    }

    @AllArgsConstructor
    @Getter
    public static class EntityRideShoulderEvent extends Cancellable {
        private Player player;
        private CompoundTag tag;
        private Shoulder shoulder;

        public enum Shoulder {
            LEFT, RIGHT
        }
    }
}
