package systems.conduit.core.mixins

import com.mojang.authlib.GameProfileRepository
import com.mojang.authlib.minecraft.MinecraftSessionService
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService
import com.mojang.datafixers.DataFixer
import net.minecraft.commands.Commands
import net.minecraft.server.MinecraftServer
import net.minecraft.server.dedicated.DedicatedServer
import net.minecraft.server.level.progress.ChunkProgressListenerFactory
import net.minecraft.server.players.GameProfileCache
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import systems.conduit.core.Conduit
import systems.conduit.core.ShutdownRunnable
import java.io.File
import java.net.Proxy

@Mixin(value = [DedicatedServer::class], remap = false)
abstract class StartupMixin(file: File?, proxy: Proxy?, dataFixer: DataFixer?, commands: Commands?, yas: YggdrasilAuthenticationService?, mss: MinecraftSessionService?, gpr: GameProfileRepository?, gpc: GameProfileCache?, cplf: ChunkProgressListenerFactory?, s: String?): MinecraftServer(file, proxy, dataFixer, commands, yas, mss, gpr, gpc, cplf, s) {

    @Inject(method = ["stopServer"], at = [At("HEAD")])
    private fun stopServer(ci: CallbackInfo) {
        // Disable plugins on shutdown
        Conduit.pluginManager.disablePlugins()
    }

    @Inject(method = ["initServer"], at = [At("HEAD")])
    private fun initServer(callback: CallbackInfoReturnable<Boolean>) {
        Conduit.setupLogger()
        Conduit.loadConfiguration()
        Conduit.server = this as systems.conduit.api.MinecraftServer
        Conduit.logger.info("Server starting initialization...")
        Runtime.getRuntime().addShutdownHook(Thread(ShutdownRunnable()))
        Conduit.commandManager.loadDefaultCommands()
        Conduit.pluginManager.loadPlugins()
    }
}
