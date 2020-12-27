package systems.conduit.main.mixins.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.LevelWriter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.events.types.PlayerEvents;

import javax.annotation.Nullable;

/**
 * @author Innectic
 * @since 12/27/2020
 */
@Mixin(value = FishingHook.class, remap = false)
public abstract class FishingHookMixin {

    @Shadow @Nullable public abstract Player getPlayerOwner();

    @Redirect(method = "retrieve", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelWriter;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"))
    public boolean addFreshEntity(LevelWriter levelWriter, Entity entity) {
        if (entity instanceof ItemEntity) {
            PlayerEvents.CaughtFishEvent event = new PlayerEvents.CaughtFishEvent((systems.conduit.main.api.mixins.Player) this.getPlayerOwner(), (ItemEntity) entity);
            Conduit.getEventManager().dispatchEvent(event);

            if (!event.isCanceled()) entity.level.addFreshEntity(entity);
            return true;
        }
        return true;
    }
}
