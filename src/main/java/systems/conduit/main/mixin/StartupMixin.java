package systems.conduit.main.mixin;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.datafixers.DataFixer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerSettings;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.players.GameProfileCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import java.io.File;

@Mixin(value = DedicatedServer.class, remap = false)
public class StartupMixin {

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void init(File file, DedicatedServerSettings dedicatedServerSettings, DataFixer dataFixer, YggdrasilAuthenticationService yggdrasilAuthenticationService, MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository, GameProfileCache gameProfileCache, ChunkProgressListenerFactory chunkProgressListenerFactory, String s, CallbackInfo ci) {
        // Disable plugins on shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> Conduit.pluginLoader.disablePlugins()));
    }

    @Inject(method = "stopServer", at = @At("HEAD"))
    private void stopServer(CallbackInfo ci) {
        // Disable plugins on shutdown
        Conduit.pluginLoader.disablePlugins();
    }

    @Inject(method = "initServer", at = @At("HEAD"))
    private void initServer(CallbackInfoReturnable<Boolean> callback) {
        Conduit.LOGGER.info("Server starting initialization...");
        Conduit.LOGGER.info("Registering events...");
        Conduit.LOGGER.info("Loading plugins...");
        Conduit.pluginLoader.loadPlugins();
    }
}
