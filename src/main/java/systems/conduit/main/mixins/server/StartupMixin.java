package systems.conduit.main.mixins.server;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerResources;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.TestSchema;

import java.net.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Mixin(value = DedicatedServer.class, remap = false)
public abstract class StartupMixin extends MinecraftServer {

    public StartupMixin(Thread thread, RegistryAccess.RegistryHolder registryHolder,
                        LevelStorageSource.LevelStorageAccess levelStorageAccess, WorldData worldData, PackRepository packRepository,
                        Proxy proxy, DataFixer dataFixer, ServerResources serverResources, MinecraftSessionService minecraftSessionService,
                        GameProfileRepository gameProfileRepository, GameProfileCache gameProfileCache,
                        ChunkProgressListenerFactory chunkProgressListenerFactory) {
        super(thread, registryHolder, levelStorageAccess, worldData, packRepository, proxy, dataFixer, serverResources,
                minecraftSessionService, gameProfileRepository, gameProfileCache, chunkProgressListenerFactory);
    }

    @Inject(method = "stopServer", at = @At("HEAD"))
    private void stopServer(CallbackInfo ci) {
        // Disable plugins on shutdown
        Conduit.getPluginManager().disablePlugins();
    }

    @Inject(method = "initServer", at = @At("HEAD"))
    private void initServer(CallbackInfoReturnable<Boolean> callback) {
        Conduit.setupLogger();
        Conduit.loadConfiguration();
        Conduit.setServer((systems.conduit.main.api.mixins.MinecraftServer) this);
        Conduit.getLogger().info("Server starting initialization...");
        Conduit.getCommandManager().loadDefaultCommands();
        Conduit.getPluginManager().loadPlugins();

        TestSchema schema = new TestSchema("MyName", UUID.randomUUID(), 127);
        Map<String, Object> serialized = schema.serialize();
        serialized.forEach((key, val) -> System.out.println(key + " -> " + val));

        Map<String, Object> feeding = new HashMap<>();
        feeding.put("uuid", serialized.get("uuid").toString());
        feeding.put("name", serialized.get("name").toString());
        feeding.put("level", serialized.get("level"));

        Optional<TestSchema> another = TestSchema.of(TestSchema.class, feeding);
        System.out.println("UUID: " + another.get().getUuid());
        System.out.println("Name: " + another.get().getName());
        System.out.println("Level: " + another.get().getLevel());
    }
}
