package systems.conduit.versions.v116.common.mixins.level.block;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.events.types.WorldEvents;

/**
 * @author Innectic
 * @since 12/27/2020
 */
@Mixin(value = ItemFrame.class, remap = false)
public abstract class ItemFrameMixin {

    @Shadow public abstract ItemStack getItem();

    @Inject(method = "dropItem(Lnet/minecraft/world/entity/Entity;Z)V", at = @At("HEAD"), cancellable = true)
    public void dropItem(Entity entity, boolean breakItemFrame, CallbackInfo ci) {
        HangingEntity hanging = (HangingEntity) (Object) this;
        WorldEvents.ItemFrameDropItemEvent event = new WorldEvents.ItemFrameDropItemEvent(hanging.getPos(), this.getItem());
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) ci.cancel();
    }

    @Inject(method = "setItem(Lnet/minecraft/world/item/ItemStack;Z)V", at = @At("HEAD"), cancellable = true)
    public void setItem(ItemStack itemStack, boolean b, CallbackInfo ci) {
        HangingEntity hanging = (HangingEntity) (Object) this;
        WorldEvents.ItemFrameDisplayItemEvent event = new WorldEvents.ItemFrameDisplayItemEvent(hanging.getPos(), itemStack);
        Conduit.getEventManager().dispatchEvent(event);

        if (event.isCanceled()) ci.cancel();
    }
}
