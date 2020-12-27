package systems.conduit.main.mixins.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.npc.Villager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.events.types.EntityEvents;

/**
 * @author Innectic
 * @since 12/26/2020
 */
@Mixin(value = Zombie.class, remap = false)
public class ZombieMixin {

    @Inject(method = "killed", at = @At("HEAD"), cancellable = true)
    public void onZombieKill(ServerLevel level, LivingEntity killed, CallbackInfo ci) {
        EntityEvents.ZombieKillEntityEvent event = new EntityEvents.ZombieKillEntityEvent((systems.conduit.main.api.mixins.ServerLevel) level, (Zombie) ((Object) this), (systems.conduit.main.api.mixins.LivingEntity) killed);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) ci.cancel();
    }

    @Inject(method = "killed", at = @At(value = "HEAD", target = "Lnet/minecraft/world/entity/monster/ZombieVillager;finalizeSpawn(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/world/entity/MobSpawnType;Lnet/minecraft/world/entity/SpawnGroupData;Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/world/entity/SpawnGroupData;"), cancellable = true)
    public void killedVillager(ServerLevel level, LivingEntity livingEntity, CallbackInfo ci) {
        EntityEvents.VillagerZombieConversionEvent event = new EntityEvents.VillagerZombieConversionEvent((systems.conduit.main.api.mixins.ServerLevel) level, (Zombie) ((Object) this), (Villager) livingEntity);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) ci.cancel();
    }
}
