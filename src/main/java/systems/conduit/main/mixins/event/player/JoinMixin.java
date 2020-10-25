package systems.conduit.main.mixins.event.player;

import net.minecraft.network.Connection;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.Player;
import systems.conduit.main.api.factories.LevelDataFactory;
import systems.conduit.main.events.types.PlayerEvents;

import java.util.Random;
import java.util.UUID;

@Mixin(value = PlayerList.class, remap = false)
public abstract class JoinMixin {

    @Shadow public abstract void broadcastMessage(Component var1, ChatType var2, UUID var3);

    @Redirect(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/ChatType;Ljava/util/UUID;)V"))
    private void playerJoinMessage(PlayerList playerList, Component component, ChatType chatType, UUID uuid) {
    }

    @Inject(method = "placeNewPlayer", at = @At(value = "TAIL"))
    private void onPlayerJoined(Connection connection, ServerPlayer serverPlayer, CallbackInfo ci) {
        PlayerEvents.PlayerJoinEvent event = new PlayerEvents.PlayerJoinEvent((Player) serverPlayer, new TranslatableComponent("multiplayer.player.joined", serverPlayer.getDisplayName())); // TODO: Join message when renamed
        Conduit.getEventManager().dispatchEvent(event);
        Component eventMessage = event.getMessage();
        if (eventMessage != null) this.broadcastMessage(event.getMessage(), ChatType.CHAT, UUID.randomUUID());

        Conduit.getLevelManager().createLevel(LevelDataFactory.builder()
                .allowCommands(true)
                .ambientLight(7)
                .bedWorks(true)
                .createDragonFight(true)
                .difficulty(Difficulty.HARD)
                .gameType(GameType.SURVIVAL)
                .dimensionType(DimensionType.OVERWORLD_LOCATION)
                .seed(new Random().nextLong())
                .generateBonusChest(true)
                .hasRaids(false)
                .hasSkylight(true)
                .natural(true)
                .logicalHeight(100)
                .levelName("testing")
                .hardcore(true)
                .hasCeiling(false)
                .build()).ifPresent(world -> {
                    System.out.println("YEET WE HAVE WORLD " + world);
                    serverPlayer.teleportTo(world, 0, 100, 0, 0, 0);
        });
    }
}
