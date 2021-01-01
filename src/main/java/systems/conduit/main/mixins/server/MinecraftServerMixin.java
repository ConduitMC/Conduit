package systems.conduit.main.mixins.server;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.mixins.MinecraftServer;
import systems.conduit.main.console.Console;
import systems.conduit.main.core.events.types.ServerEvents;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;

@Mixin(value = net.minecraft.server.MinecraftServer.class, remap = false)
public abstract class MinecraftServerMixin implements MinecraftServer {

    @Accessor public abstract Executor getExecutor();
    @Accessor public abstract Map<ResourceKey<Level>, ServerLevel> getLevels();
    @Accessor public abstract RegistryAccess.RegistryHolder getRegistryHolder();
    @Accessor public abstract ChunkProgressListenerFactory getProgressListenerFactory();
    @Accessor public abstract LevelStorageSource.LevelStorageAccess getStorageSource();

    @Shadow public abstract ServerLevel getLevel(ResourceKey<Level> dimensionType);

    @Shadow public abstract Commands getCommands();
    @Shadow public abstract boolean isStopped();
    @Shadow public abstract boolean isRunning();
    @Shadow public abstract CommandSourceStack createCommandSourceStack();
    @Shadow public abstract void close();
    @Shadow public abstract Iterable<ServerLevel> getAllLevels();
    @Shadow public abstract WorldData getWorldData();

    @Shadow private ProfilerFiller profiler;
    @Shadow private int tickCount;
    @Shadow public abstract PlayerList getPlayerList();
    @Shadow public abstract ServerStatus getStatus();

    @Inject(method = "runServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;updateStatusIcon(Lnet/minecraft/network/protocol/status/ServerStatus;)V"))
    private void onRead(CallbackInfo ci) {
        Console.createConsole();
    }

    @Inject(method = "stopServer", at = @At("HEAD"))
    public void stopServer(CallbackInfo ci) {
        Conduit.getEventManager().dispatchEvent(new ServerEvents.ServerShuttingDownEvent());
    }

    @Inject(method = "initServer", at = @At("RETURN"))
    public void initServer(CallbackInfoReturnable<Boolean> cir) {
        Conduit.getEventManager().dispatchEvent(new ServerEvents.ServerInitializedEvent());
    }

    @Inject(method = "runServer", at = @At("HEAD"))
    public void runServer(CallbackInfo ci) {
        Conduit.getEventManager().dispatchEvent(new ServerEvents.ServerStartingEvent());
    }

    /**
     * @author ConduitMC
     */
    @Overwrite
    public String getServerModName() {
        return "Conduit";
    }

    @Inject(method = "tickServer", at = @At(value = "TAIL"))
    public void tickServer(BooleanSupplier booleanSupplier, CallbackInfo ci) {
        this.profiler.push("plugin runnables");

        Conduit.getRunnableManager().getActiveRunnables().values().stream()
                .filter(runnableData -> this.tickCount % runnableData.getEvery() == 0)
                .filter(data -> !data.isRunning()).forEach(entry -> {
                    if (entry.isAsync()) CompletableFuture.runAsync(entry.getRunnable());
                    else entry.getRunnable().run();
        });

        this.profiler.pop();
    }
}
