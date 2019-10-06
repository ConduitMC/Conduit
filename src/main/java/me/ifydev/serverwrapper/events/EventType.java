package me.ifydev.serverwrapper.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Innectic
 * @since 10/2/2019
 */
public abstract class EventType {

    @AllArgsConstructor
    @Getter
    public static class BlockInteractEvent extends EventType {
        private Player player;
        private BlockState blockState;
        private InteractionHand hand;
    }

    @AllArgsConstructor
    @Getter
    public static class BlockBreakEvent extends EventType {
        private Player player;
        private BlockState blockState;
    }

    @AllArgsConstructor
    @Getter
    public static class BlockPlaceEvent extends EventType {
        private Player player;
        private BlockState blockState;
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerJoinEvent extends EventType {
        private Player player;
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerLeaveEvent extends EventType {
        private Player player;
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
    public static class PlayerDamageByEntityEvent extends EventType {
        private Player damaged;
        private Entity damager;
        private float damage;
        private DamageMeta meta;
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerDamageByPlayerEvent extends EventType {
        private Player damaged;
        private Player damager;
        private float damage;
        private DamageMeta meta;
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerDamageByArrowEvent extends EventType {
        private Player damaged;
        private Entity shooter;
        private AbstractArrow arrow;
        private float damage;
        private DamageMeta meta;
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerGameModeChangeEvent extends EventType {
        private Player player;
        private GameType gamemode;
    }
}
