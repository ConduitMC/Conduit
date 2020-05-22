package systems.conduit.core.mixins

import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandSource
import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.world.entity.Entity
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect
import systems.conduit.core.Conduit

@Mixin(value = [CommandSourceStack::class], remap = false)
abstract class CommandSourceMixin {

    @Shadow @Final
    private val source: CommandSource? = null

    @Shadow @Final
    private val entity: Entity? = null

    @Redirect(method = ["sendSuccess"], at = At(value = "INVOKE", target = "Lnet/minecraft/commands/CommandSource;sendMessage(Lnet/minecraft/network/chat/Component;)V"))
    private fun sendSuccess(source: CommandSource, component: Component) {
        sendColoredString(component)
    }

    @Redirect(method = ["sendFailure"], at = At(value = "INVOKE", target = "Lnet/minecraft/commands/CommandSource;sendMessage(Lnet/minecraft/network/chat/Component;)V"))
    private fun sendFailure(source: CommandSource, component: Component) {
        sendColoredString(TextComponent("").append(component).withStyle(ChatFormatting.RED))
    }

    private fun sendColoredString(component: Component) {
        if (entity == null) {
            Conduit.logger.info(TextComponent(component.coloredString).text)
        } else {
            source?.sendMessage(component)
        }
    }
}
