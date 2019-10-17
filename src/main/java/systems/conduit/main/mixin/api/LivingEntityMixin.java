package systems.conduit.main.mixin.api;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.SharedMonsterAttributes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import systems.conduit.main.api.LivingEntity;

@Mixin(value = net.minecraft.world.entity.LivingEntity.class)
public abstract class LivingEntityMixin implements LivingEntity {

    @Shadow public abstract float getHealth();
    @Shadow public abstract void setHealth(float health);

    @Shadow @Final public abstract float getMaxHealth();

    @Shadow public abstract AttributeInstance getAttribute(Attribute attribute);

    public void setMaxHealth(float health) {
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
    }
}
