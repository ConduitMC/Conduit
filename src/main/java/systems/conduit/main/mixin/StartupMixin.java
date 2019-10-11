package systems.conduit.main.mixin;

import systems.conduit.main.Conduit;

import net.minecraft.server.dedicated.DedicatedServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = DedicatedServer.class, remap = false)
public class StartupMixin {

    @Inject(method = "initServer", at = @At("HEAD"))
    private void initServer(CallbackInfoReturnable<Boolean> callback) {
        Conduit.LOGGER.info("Server starting initialization...");
        Conduit.LOGGER.info("Registering events...");

        Conduit.pluginLoader.loadPlugins();
    }
}
