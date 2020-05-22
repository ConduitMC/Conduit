package systems.conduit.core.mixins

import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.Commands
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.profiling.GameProfiler
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.storage.LevelStorageSource
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Overwrite
import org.spongepowered.asm.mixin.Shadow
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
import systems.conduit.api.MinecraftServer
import systems.conduit.core.Conduit.eventManager
import systems.conduit.core.console.Console.createConsole
import systems.conduit.core.events.types.ServerEvents.*
import java.util.concurrent.Executor


@Mixin(value = [net.minecraft.server.MinecraftServer::class], remap = false)
abstract class MinecraftServerMixin: MinecraftServer {

    @Shadow
    @Final
    private val executor: Executor? = null

    @Shadow
    @Final
    private val levels: MutableMap<DimensionType?, ServerLevel?>? = null

    @Shadow
    abstract override fun getLevel(dimensionType: DimensionType?): ServerLevel?

    @Shadow
    abstract override fun getStorageSource(): LevelStorageSource?

    @Shadow
    abstract override fun getProfiler(): GameProfiler?

    @Shadow
    abstract override fun getCommands(): Commands

    @Shadow
    abstract override fun isStopped(): Boolean

    @Shadow
    abstract override fun isRunning(): Boolean

    @Shadow
    abstract override fun createCommandSourceStack(): CommandSourceStack?

    @Shadow
    abstract override fun close()

    @Inject(method = ["run"], at = [At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;updateStatusIcon(Lnet/minecraft/network/protocol/status/ServerStatus;)V")])
    private fun onRead(ci: CallbackInfo) {
        createConsole()
    }

    override fun getExecutor(): Executor? {
        return executor
    }

    override fun getLevels(): MutableMap<DimensionType?, ServerLevel?> {
        return levels!!
    }

    @Inject(method = ["stopServer"], at = [At("HEAD")])
    fun stopServer(ci: CallbackInfo?) {
        eventManager.dispatchEvent(ServerShuttingDownEvent())
    }

    @Inject(method = ["initServer"], at = [At("RETURN")])
    fun initServer(cir: CallbackInfoReturnable<Boolean?>?) {
        eventManager.dispatchEvent(ServerInitializedEvent())
    }

    @Inject(method = ["run"], at = [At("HEAD")])
    fun run(ci: CallbackInfo?) {
        eventManager.dispatchEvent(ServerStartingEvent())
    }

    /**
     * @author ConduitMC
     */
    @Overwrite
    override fun getServerModName(): String? {
        return "Conduit"
    }
}
