package me.ifydev.conduit.mixin;

import me.ifydev.conduit.Conduit;
import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Innectic
 * @since 10/5/2019
 */
@Mixin(value = DedicatedServer.class, remap = false)
public class StartupMixin {

    @Inject(method = "initServer", at = @At("HEAD"))
    public void initServer(CallbackInfoReturnable<Boolean> callback) {
        Conduit.LOGGER.info("Server starting initialization...");
        Conduit.LOGGER.info("Registering events...");
    }
}
