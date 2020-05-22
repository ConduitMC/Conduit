package systems.conduit.core.mixins.event.player

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.level.ServerPlayerGameMode
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import systems.conduit.core.Conduit
import systems.conduit.core.events.types.WorldEvents

@Mixin(value = [ServerPlayerGameMode::class], remap = false)
abstract class ServerPlayerGameModeMixin {

    @Shadow var player: ServerPlayer? = null

    @Shadow var level: ServerLevel? = null

    @Inject(method = ["destroyBlock"], at = [At("HEAD")])
    private fun destroyAndAck(blockPos: BlockPos, cir: CallbackInfoReturnable<Boolean>) {
        // TODO: Event cancellations
        if (level != null) {
            val event = WorldEvents.BlockBreakEvent(player as systems.conduit.api.ServerPlayer, level!!.getBlockState(blockPos))
            Conduit.eventManager.dispatchEvent(event)
            if (event.cancelled) {
                return
            }
        }
    }
}
