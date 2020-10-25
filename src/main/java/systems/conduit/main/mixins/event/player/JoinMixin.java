package systems.conduit.main.mixins.event.player;

import net.minecraft.network.Connection;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.Player;
import systems.conduit.main.api.factories.InventoryFactory;
import systems.conduit.main.events.types.PlayerEvents;
import systems.conduit.main.inventory.CustomInventory;

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

//        Conduit.getLevelManager().createLevel(LevelDataFactory.builder()
//                .allowCommands(true).ambientLight(7).bedWorks(true).createDragonFight(false).difficulty(Difficulty.HARD)
//                .gameType(GameType.SURVIVAL).dimensionType(DimensionType.OVERWORLD_LOCATION).seed(new Random().nextLong())
//                .generateBonusChest(false).hasRaids(false).hasSkylight(true).natural(true).logicalHeight(256)
//                .levelName("testing").hardcore(false).hasCeiling(false)
//                .build()).ifPresent(world -> serverPlayer.teleportTo(world, 0, 100, 0, 0, 0));
//
//        EntityFactory.builder()
//                .level(serverPlayer.getLevel()).position(new BlockPos(serverPlayer.position())).type(EntityType.CREEPER)
//                .build().spawn();

//        ChestContainer container = ChestContainer.create(MenuType.GENERIC_9x6, "Testing!");
//
//        container.setItem(4, new ItemStack(Items.DIRT, 27));
//        container.setItem(4, new ItemStack(Items.DIAMOND_BLOCK));
//
//        container.addListener(container1 -> {
//            System.out.println("Testing");
//        });
//        ((Player) serverPlayer).openContainer(container);

        CustomInventory inv = InventoryFactory.builder()
            .name("Testing!")
            .menuType(MenuType.GENERIC_9x2)
            .build()
            .set(1, new ItemStack(Items.DIRT, 5))
            .changed((player, clicked, m) -> player.down().sendMessage(new TextComponent("WOW YOU MIDDLE CLICKED A THING! " + clicked), UUID.randomUUID()))
            .register();
        inv.open((systems.conduit.main.api.ServerPlayer) serverPlayer);
    }
}
