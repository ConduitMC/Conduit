package systems.conduit.main.events.types;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.GameType;
import systems.conduit.main.api.Player;
import systems.conduit.main.events.Cancellable;
import systems.conduit.main.events.EventType;

/**
 * @author Innectic
 * @since 10/21/2019
 */
public class PlayerEvents {

    @AllArgsConstructor
    @Getter
    public static class PlayerJoinEvent extends EventType {
        private Player player;
        @Setter private Component message;
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
    public static class PlayerDamageByEntityEvent extends Cancellable {
        private Player damaged;
        private Entity damager;
        private float damage;
        private DamageMeta meta;
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerDamageByPlayerEvent extends Cancellable {
        private Player damaged;
        private Player damager;
        private float damage;
        private DamageMeta meta;
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerDamageByArrowEvent extends Cancellable {
        private Player damaged;
        private Entity shooter;
        private AbstractArrow arrow;
        private float damage;
        private DamageMeta meta;
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerGameModeChangeEvent extends Cancellable {
        private Player player;
        @Setter private GameType gamemode;
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerChatEvent extends Cancellable {
        private Player player;
        @Setter private Component message;
    }

    @AllArgsConstructor
    @Getter
    public static class PlayerCommandEvent extends Cancellable {
        private Player player;
        @Setter private String message;
    }
}
