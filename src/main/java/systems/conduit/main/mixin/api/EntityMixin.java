package systems.conduit.main.mixin.api;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

/**
 * @author Innectic
 * @since 10/12/2019
 */
@Mixin(value = Entity.class, remap = false)
public abstract class EntityMixin implements systems.conduit.main.api.Entity {

    @Shadow public Level level;

    @Shadow public abstract UUID getUUID();
    @Shadow public abstract void sendMessage(Component component);

    @Override
    public void sendMessage(String message) {
        sendMessage(new TextComponent(message));
    }

    @Override
    public Level getLevel() {
        return level;
    }
}
