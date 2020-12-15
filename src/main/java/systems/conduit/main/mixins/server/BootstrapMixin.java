package systems.conduit.main.mixins.server;

import net.minecraft.commands.synchronization.ArgumentTypes;
import net.minecraft.server.Bootstrap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.core.commands.PlayerArgumentSerializer;
import systems.conduit.main.core.commands.PlayerArgumentType;

/**
 * @author Innectic
 * @since 12/15/2020
 */
@Mixin(value = Bootstrap.class, remap = false)
public class BootstrapMixin {

    @Inject(method = "bootStrap", at = @At(value = "TAIL", target = "Lnet/minecraft/commands/synchronization/ArgumentTypes;bootStrap()V"))
    private static void bootStrap(CallbackInfo ci) {
        ArgumentTypes.register("player", PlayerArgumentType.class, new PlayerArgumentSerializer());
    }
}
