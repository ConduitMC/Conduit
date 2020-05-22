package systems.conduit.core.mixins.event

import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.Pose
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseOnContext
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import systems.conduit.api.ServerPlayer
import systems.conduit.core.Conduit
import systems.conduit.core.events.types.WorldEvents

@Mixin(value = [ItemStack::class], remap = false)
class ItemStackMixin {

    @Inject(method = ["useOn"], at = [At("HEAD")])
    private fun useOn(context: UseOnContext, cir: CallbackInfoReturnable<InteractionResult>) {
        val isCrouching = context.player != null && context.player!!.pose != null && context.player!!.pose == Pose.values()[5]
        val event = WorldEvents.BlockInteractEvent(context.player as ServerPlayer,
                context.level.getBlockState(context.clickedPos), context.hand, context.itemInHand,
                context.clickedFace, isCrouching, context.isInside)
        Conduit.eventManager.dispatchEvent(event)
    }
}
