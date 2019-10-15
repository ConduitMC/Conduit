package systems.conduit.main.mixin;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import net.minecraft.commands.Commands;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.players.GameProfileCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;

import java.io.File;
import java.net.Proxy;
import java.util.Optional;

@Mixin(value = DedicatedServer.class, remap = false)
public abstract class StartupMixin extends MinecraftServer {

    public StartupMixin(File file, Proxy proxy, com.mojang.datafixers.DataFixer dataFixer, Commands commands, YggdrasilAuthenticationService yggdrasilAuthenticationService, MinecraftSessionService minecraftSessionService, GameProfileRepository gameProfileRepository, GameProfileCache gameProfileCache, ChunkProgressListenerFactory chunkProgressListenerFactory, String s) {
        super(file, proxy, dataFixer, commands, yggdrasilAuthenticationService, minecraftSessionService, gameProfileRepository, gameProfileCache, chunkProgressListenerFactory, s);
    }

    @Inject(method = "stopServer", at = @At("HEAD"))
    private void stopServer(CallbackInfo ci) {
        // Disable plugins on shutdown
        Conduit.pluginLoader.disablePlugins();
    }

    @Inject(method = "initServer", at = @At("HEAD"))
    private void initServer(CallbackInfoReturnable<Boolean> callback) {
        Conduit.LOGGER.info("Server starting initialization...");
        Conduit.server = Optional.of(this);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> Conduit.pluginLoader.disablePlugins()));
        Conduit.LOGGER.info("Registering events...");
        Conduit.LOGGER.info("Loading plugins...");
        Conduit.pluginLoader.loadPlugins();
    }
}
