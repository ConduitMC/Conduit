package systems.conduit.main.mixin;

import net.minecraft.server.dedicated.DedicatedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;

@Mixin(value = DedicatedServer.class, remap = false)
public class StartupMixin {

    @Inject(method = "stopServer", at = @At("HEAD"))
    private void stopServer(CallbackInfo ci) {
        // Disable plugins on shutdown
        Conduit.pluginManager.disablePlugins();
    }

    @Inject(method = "initServer", at = @At("HEAD"))
    private void initServer(CallbackInfoReturnable<Boolean> callback) {
        Conduit.LOGGER.info("Server starting initialization...");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Conduit.pluginManager.disablePlugins()));
        Conduit.LOGGER.info("Registering events...");
        Conduit.LOGGER.info("Loading plugins...");
        Conduit.pluginManager.loadPlugins();
    }
}
