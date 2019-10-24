package systems.conduit.main.mixin.event.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import systems.conduit.main.Conduit;
import systems.conduit.main.events.types.EntityEvents;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Innectic
 * @since 10/21/2019
 */
@Mixin(value = Slime.class, remap = false)
public abstract class SlimeMixin extends Mob {

    private List<Slime> slimes = new ArrayList<>();

    protected SlimeMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "remove",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelWriter;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z")
    )
    public void remove(CallbackInfo ci, Slime slime) {
        slimes.add(slime);
    }

    @Inject(method = "remove", at = @At("TAIL"))
    public void shadow$remove(CallbackInfo ci) {
        EntityEvents.SlimeSplitEvent event = new EntityEvents.SlimeSplitEvent((Slime) ((Object) this), slimes);
        Conduit.getEventManager().dispatchEvent(event);

        slimes.forEach(Slime::kill);
    }
}
