package systems.conduit.main.mixins.player;

import lombok.Getter;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundPlayerAbilitiesPacket;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.TextFilter;
import net.minecraft.stats.ServerRecipeBook;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.ChatVisiblity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import systems.conduit.main.Conduit;
import systems.conduit.main.api.mixins.ServerPlayer;
import systems.conduit.main.core.events.types.PlayerEvents;
import systems.conduit.main.core.permissions.PermissionNode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Mixin(value = net.minecraft.server.level.ServerPlayer.class, remap = false)
public abstract class ServerPlayerMixin implements ServerPlayer {

    @Shadow public ServerGamePacketListenerImpl connection;

    @Accessor public abstract int getContainerCounter();
    @Accessor public abstract ServerPlayerGameMode getGameMode();
    @Shadow protected abstract void nextContainerCounter();
    @Shadow public abstract void teleportTo(ServerLevel level, double x, double y, double z, float pitch, float yaw);
    @Shadow @Final public ServerPlayerGameMode gameMode;
    @Shadow public abstract void onUpdateAbilities();
    @Shadow public abstract void sendMessage(Component component, UUID uuid);
    @Shadow public abstract void sendTexturePack(String s, String s1, boolean b);

    @Shadow public abstract ChatVisiblity getChatVisibility();

    @Shadow protected abstract int getPermissionLevel();

    @Shadow public abstract void resetLastActionTime();
    @Shadow public abstract long getLastActionTime();
    @Shadow public abstract ServerStatsCounter getStats();
    @Shadow public abstract ServerRecipeBook getRecipeBook();
    @Shadow public abstract PlayerAdvancements getAdvancements();

    @Shadow public abstract void sendRemoveEntity(Entity entity);
    @Shadow public abstract void cancelRemoveEntity(Entity entity);

    @Shadow public abstract Entity shadow$getCamera();
    @Shadow public abstract void setCamera(Entity entity);

    @Shadow public abstract void untrackChunk(ChunkPos pos);
    @Shadow public abstract SectionPos getLastSectionPos();
    @Shadow public abstract void setLastSectionPos(SectionPos pos);
    @Shadow public abstract TextFilter shadow$getTextFilter();

    @Getter private List<PermissionNode> permissionNodes = new ArrayList<>();

    @Override
    public final void sendMessage(String message) {
        this.sendMessage(new TextComponent(message), Util.NIL_UUID);
    }

    @Override
    public final void sendUpdatedAbilities() {
        down().connection.send(new ClientboundPlayerAbilitiesPacket((Abilities) getAbilities()));
    }

    @Override
    public void addPermission(String permission) {
        permissionNodes.add(new PermissionNode(permission));
    }

    @Override
    public void removePermission(String permission) {
        permissionNodes.removeIf(n -> n.getPermission().equalsIgnoreCase(permission));
    }

//    @Override
//    public systems.conduit.main.api.mixins.Entity getCamera() {
//        return (systems.conduit.main.api.mixins.Entity) shadow$getCamera();
//    }
//
//    @Override
//    public Optional<TextFilter> getTextFilter() {
//        return Optional.ofNullable(shadow$getTextFilter());
//    }

    @Override
    public boolean hasPermission(String permission) {
        return permissionNodes.stream().anyMatch(p -> p.applies(permission));
    }

    @Override
    public boolean isOp() {
        return Conduit.getServer().map(s -> s.getPlayerList().isOp(this.down().getGameProfile())).orElse(false);
    }

    @Override
    public void sendResourcePack(String url, String hash, boolean required) {
        this.sendTexturePack(url, hash, required);
    }

    @Override
    public net.minecraft.server.level.ServerPlayer down() {
        return (net.minecraft.server.level.ServerPlayer) (Object) this;
    }

    @Override
    public GameType getGameType() {
        return this.getGameMode().getGameModeForPlayer();
    }

    @Override
    public void kick(TextComponent kickMessage) {
        this.connection.disconnect(kickMessage);
    }

    @Override
    public int getOpPermissionLevel() {
        return getPermissionLevel();
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
        PlayerEvents.SpectateEvent event = new PlayerEvents.SpectateEvent((ServerPlayer) (Object) this, entity);
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

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    public void readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("ConduitPermissions", 10)) {
            CompoundTag permissionsTag = tag.getCompound("ConduitPermissions");

            List<String> keys = new ArrayList<>(permissionsTag.getAllKeys());
            keys.forEach(permission -> {
                if (!permissionsTag.getBoolean(permission)) return;
                this.addPermission(permission);
            });
        }

        permissionNodes.forEach(permission -> permission.applies("conduit.testing"));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    public void addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        CompoundTag permissionsTag = new CompoundTag();
        permissionNodes.forEach(node -> permissionsTag.putBoolean(node.getPermission(), true));
        tag.put("ConduitPermissions", permissionsTag);
    }

    @Inject(method = "drop", at = @At("HEAD"), cancellable = true)
    public void drop(ItemStack item, boolean dropStyle, boolean setThrower, CallbackInfoReturnable<ItemEntity> cir) {
        PlayerEvents.DropItemEvent event = new PlayerEvents.DropItemEvent((ServerPlayer) ((Object) this), item);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) {
            cir.setReturnValue(null);
            cir.cancel();
        }
    }
}
