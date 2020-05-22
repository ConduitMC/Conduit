package systems.conduit.core.events.types

import net.minecraft.core.BlockPos
import net.minecraft.core.Location
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.GameType
import systems.conduit.api.Level
import systems.conduit.api.Player
import systems.conduit.api.ServerPlayer
import systems.conduit.core.events.Cancellable

/*
 * @author Innectic
 * @since 10/21/2019
 */
class PlayerEvents {

    /**
     * Player join server event.
     * Mixin implementation: [systems.conduit.core.mixins.event.player.JoinMixin]
     */
    class PlayerJoinEvent(val player: Player, var message: Component?): EventType()

    /**
     * Player leave server event.
     * This is not currently implemented.
     */
    class PlayerLeaveEvent(val player: Player, val message: Component?, val type: LeaveType): EventType()

    /**
     * Meta information provided by Minecraft.
     */
    enum class LeaveType {
        LEFT,  // Manually left the game
        KICKED,  // Got kicked by operator or plugin
        UNKNOWN // Unknown. Possibly should have a type for it if its common.
    }

    /**
     * Meta information provided by Minecraft.
     */
    data class DamageMeta(val isThorns: Boolean = false, val bypassArmor: Boolean = false,
                          val bypassInvulnerability: Boolean = false, val bypassMagic: Boolean = false,
                          val isCreativePlayer: Boolean = false, val isExplosion: Boolean = false, val isFire: Boolean = false,
                          val isMagic: Boolean = false, val isProjectile: Boolean = false)

    /**
     * Player damage by entity event.
     * Mixin implementation: [systems.conduit.core.mixins.event.player.DamageMixin]
     */
    class PlayerDamageByEntityEvent(val damaged: Player, val damager: Entity?, val damage: Float = 0F, val meta: DamageMeta): Cancellable()

    /**
     * Player damage by another player event.
     * Mixin implementation: [systems.conduit.core.mixins.event.player.DamageMixin]
     */
    class PlayerDamageByPlayerEvent(val damaged: Player, val damager: ServerPlayer?, val damage: Float = 0F, val meta: DamageMeta): Cancellable()

    /**
     * Player damage by arrow event. This can be either another player, skeleton, etc.
     * Mixin implementation: [systems.conduit.core.mixins.event.player.DamageMixin]
     */
    class PlayerDamageByArrowEvent(val damaged: Player, val shooter: Entity?, val arrow: AbstractArrow, val damage: Float = 0F, val meta: DamageMeta): Cancellable()

    /**
     * Player game mode change event.
     * Mixin implementation: [systems.conduit.core.mixins.api.ServerPlayerMixin]
     */
    class PlayerGameModeChangeEvent(val player: Player, val gamemode: GameType): Cancellable()

    /**
     * Player chat event. No commands included.
     * Mixin implementation: [systems.conduit.core.mixins.ServerGamePacketListenerMixin]
     */
    class PlayerChatEvent(val player: Player, var message: Component?): Cancellable()

    /**
     * Player send command event.
     * Mixin implementation: [systems.conduit.core.mixins.ServerGamePacketListenerMixin]
     */
    class PlayerCommandEvent(val player: Player, var message: String): Cancellable()


    class RespawnEvent(val player: Player): EventType()

    class MoveEvent(val player: Player, val to: Location, val from: Location): Cancellable()

    class LevelSwitchEvent(val player: Player, val to: Level, val from: Level): Cancellable()

    class KickEvent(val player: Player, reason: String): EventType()

    class ConsumeEvent(val player: Player, val consumed: ItemStack): EventType()

    class FishEvent(val player: Player): Cancellable()

    class EnterBedEvent(val player: Player, val bed: BlockPos): Cancellable()

    class LeaveBedEvent(val player: Player, val bed: BlockPos): Cancellable()

    class SpectateEvent(val player: Player, val spectating: Entity): Cancellable()
}
