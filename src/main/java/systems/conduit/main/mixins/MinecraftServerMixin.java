package systems.conduit.main.mixins;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.profiling.GameProfiler;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.MinecraftServer;
import systems.conduit.main.console.Console;
import systems.conduit.main.events.types.ServerEvents;

import java.util.Map;
import java.util.concurrent.Executor;

@Mixin(value = net.minecraft.server.MinecraftServer.class, remap = false)
public abstract class MinecraftServerMixin implements MinecraftServer {

    @Shadow @Final private Executor executor;

    @Shadow public abstract ServerLevel getLevel(DimensionType dimensionType);
    @Shadow public abstract LevelStorageSource getStorageSource();

    @Shadow public abstract GameProfiler getProfiler();

    @Shadow @Final private Map<DimensionType, ServerLevel> levels;
    @Shadow public abstract Commands getCommands();
    @Shadow public abstract boolean isStopped();
    @Shadow public abstract boolean isRunning();
    @Shadow public abstract CommandSourceStack createCommandSourceStack();
    @Shadow public abstract void close();

    @Inject(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;updateStatusIcon(Lnet/minecraft/network/protocol/status/ServerStatus;)V"))
    private void onRead(CallbackInfo ci) {
        Console.createConsole();
    }

    public Executor getExecutor() {
        return executor;
   }

   public Map<DimensionType, ServerLevel> getLevels() {
        return levels;
   }

    @Inject(method = "stopServer", at = @At("HEAD"))
    public void stopServer(CallbackInfo ci) {
        Conduit.getEventManager().dispatchEvent(new ServerEvents.ServerShuttingDownEvent());
    }

    @Inject(method = "initServer", at = @At("RETURN"))
    public void initServer(CallbackInfoReturnable<Boolean> cir) {
        Conduit.getEventManager().dispatchEvent(new ServerEvents.ServerInitializedEvent());
    }

    @Inject(method = "run", at = @At("HEAD"))
    public void run(CallbackInfo ci) {
        Conduit.getEventManager().dispatchEvent(new ServerEvents.ServerStartingEvent());
    }
}
