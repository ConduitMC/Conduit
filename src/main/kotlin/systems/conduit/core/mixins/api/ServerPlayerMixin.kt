package systems.conduit.core.mixins.api

import net.minecraft.network.chat.TextComponent
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket
import net.minecraft.network.protocol.game.ClientboundOpenScreenPacket
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.level.GameType
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.ModifyVariable
import systems.conduit.api.ServerPlayer
import systems.conduit.api.inventory.ChestContainer
import systems.conduit.core.Conduit
import systems.conduit.core.events.types.PlayerEvents

@Mixin(value = [net.minecraft.server.level.ServerPlayer::class], remap = false)
abstract class ServerPlayerMixin: ServerPlayer {

    @Shadow private val containerCounter = 0

    @Shadow
    protected abstract fun nextContainerCounter()

    override fun openContainer(container: ChestContainer?) {
        closeOpenedContainer()
        nextContainerCounter()
        if (container == null) return
        // Todo: Fix casts?
        val menu: AbstractContainerMenu = ChestMenu(container.type, containerCounter, (this as Player).inventory, container, container.containerSize / 9)
        menu.addSlotListener(this as net.minecraft.server.level.ServerPlayer)
        if (connection == null) return
        connection.send(ClientboundOpenScreenPacket(menu.containerId, menu.type, TextComponent(container.title)))
        this.containerMenu = menu
        connection.send(ClientboundContainerSetContentPacket(menu.containerId, this.containerMenu.items))
    }

    @ModifyVariable(method = ["setGameMode"], at = At("HEAD"))
    private fun updateGameMode(gameType: GameType): GameType {
        // TODO: Allow this event to be cancelled
        //
        // We'll need to find a different way to hook into gamemode changes.
        val event = PlayerEvents.PlayerGameModeChangeEvent(this, gameType)
        Conduit.eventManager.dispatchEvent(event)
        return event.gamemode
    }
}