package systems.conduit.main.mixins.api;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.ServerPlayer;
import systems.conduit.main.events.types.PlayerEvents;
import systems.conduit.main.inventory.CustomInventory;
import systems.conduit.main.inventory.events.InventoryEventType;

import java.util.Optional;

@Mixin(value = net.minecraft.server.level.ServerPlayer.class, remap = false)
public abstract class ServerPlayerMixin implements ServerPlayer {

    @Shadow private int containerCounter;
    @Shadow public ServerGamePacketListenerImpl connection;

    @Shadow protected abstract void nextContainerCounter();

    @Shadow public abstract void teleportTo(ServerLevel level, double x, double y, double z, float pitch, float yaw);

    @Shadow @Final public ServerPlayerGameMode gameMode;
    @Shadow public abstract void onUpdateAbilities();

    @Override
    public int getContainerCounter() {
        return containerCounter;
    }

    @Override
    public net.minecraft.server.level.ServerPlayer down() {
        return (net.minecraft.server.level.ServerPlayer) (Object) this;
    }

    @Override
    public ServerPlayerGameMode getGameMode() {
        return this.gameMode;
    }

    @Override
    public GameType getGameType() {
        return this.getGameMode().getGameModeForPlayer();
    }

    @ModifyVariable(method = "setGameMode", at = @At("HEAD"))
    private GameType updateGameMode(GameType gameType) {
        // TODO: Allow this event to be cancelled
        //
        // We'll need to find a different way to hook into gamemode changes.

        PlayerEvents.PlayerGameModeChangeEvent event = new PlayerEvents.PlayerGameModeChangeEvent(this, gameType);
        Conduit.getEventManager().dispatchEvent(event);
        return event.getGamemode();
    }

    @Inject(method = "slotChanged", at = @At(value = "HEAD", target = "Lnet/minecraft/advancements/critereon/InventoryChangeTrigger;trigger(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/item/ItemStack;)V"))
    public void slotChanged(AbstractContainerMenu container, int slotId, ItemStack clicked, CallbackInfo ci) {
        PlayerEvents.InventoryMoveItemEvent event = new PlayerEvents.InventoryMoveItemEvent(this, container, clicked);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) return;

        Optional<CustomInventory> inventory = Conduit.getInventoryManager().findInventoryByPlayerContainerId(container.containerId);
        inventory.ifPresent(inv -> inv.pushEvent(InventoryEventType.SLOT_CHANGED, this, clicked, container));
    }

    @Inject(method = "doCloseContainer", at = @At("TAIL"))
    public void doCloseContainer(CallbackInfo ci) {
        int id = this.getContainerMenu().containerId;
        Optional<CustomInventory> inventory = Conduit.getInventoryManager().findInventoryByPlayerContainerId(id);
        inventory.ifPresent(inv -> inv.finished(id));
    }

    @Inject(method = "startSleeping", at = @At("HEAD"))
    public void startSleeping(BlockPos blockPos, CallbackInfo ci) {
        PlayerEvents.EnterBedEvent event = new PlayerEvents.EnterBedEvent(this, blockPos);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) return;
    }

    @Inject(method = "stopSleepInBed", at = @At("HEAD"))
    public void stopSleeping(CallbackInfo ci) {
        PlayerEvents.LeaveBedEvent event = new PlayerEvents.LeaveBedEvent(this, new BlockPos(this.position()));
        Conduit.getEventManager().dispatchEvent(event);
    }

    @Inject(method = "attack", at = @At(value = "HEAD", target = "Lnet/minecraft/server/level/ServerPlayer;setCamera(Lnet/minecraft/world/entity/Entity;)V"))
    public void attack(Entity entity, CallbackInfo ci) {
        PlayerEvents.SpectateEvent event = new PlayerEvents.SpectateEvent((net.minecraft.server.level.ServerPlayer) (Object) this, entity);
        Conduit.getEventManager().dispatchEvent(event);

        // If the event is cancelled, prevent the player from continuing to spectate.
        if (event.isCanceled()) return;
    }

    @Inject(method = "changeDimension", at = @At("HEAD"))
    public void changeDimension(ServerLevel destination, CallbackInfoReturnable<Entity> callback) {
        PlayerEvents.LevelSwitchEvent event = new PlayerEvents.LevelSwitchEvent(this, this.getLevel(), destination);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) return;
    }

    @Inject(method = "die", at = @At(value = "HEAD", target = "Lnet/minecraft/world/level/Level;broadcastEntityEvent(Lnet/minecraft/world/entity/Entity;B)V"))
    public void die(DamageSource damageSource, CallbackInfo ci) {
        PlayerEvents.DeathEvent event = new PlayerEvents.DeathEvent(this, this.conduit_getKillCredit(), damageSource);
        Conduit.getEventManager().dispatchEvent(event);
    }
}
