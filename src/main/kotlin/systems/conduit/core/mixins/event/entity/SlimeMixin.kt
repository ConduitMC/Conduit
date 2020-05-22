package systems.conduit.core.mixins.event.entity

import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.monster.Slime
import net.minecraft.world.level.Level
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import systems.conduit.core.Conduit
import systems.conduit.core.events.types.EntityEvents
import java.util.function.Consumer

/*
 * @author Innectic
 * @since 10/21/2019
 */
@Mixin(value = [Slime::class], remap = false)
abstract class SlimeMixin protected constructor(entityType: EntityType<out Mob?>?, level: Level?): Mob(entityType, level) {

    private val slimes: MutableList<Slime> = ArrayList()

    @Inject(method = ["remove"], at = [At(value = "INVOKE", target = "Lnet/minecraft/world/level/LevelWriter;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z")])
    fun remove(ci: CallbackInfo?, slime: Slime) {
        slimes.add(slime)
    }

    @Suppress("CAST_NEVER_SUCCEEDS", "FunctionName")
    @Inject(method = ["remove"], at = [At("TAIL")])
    fun `shadow$remove`(ci: CallbackInfo?) {
        val event = EntityEvents.SlimeSplitEvent(this as Slime, slimes)
        Conduit.eventManager.dispatchEvent(event)
        slimes.forEach(Consumer { obj: Slime -> obj.kill() })
    }
}
