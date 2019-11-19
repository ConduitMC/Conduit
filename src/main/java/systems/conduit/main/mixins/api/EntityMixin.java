package systems.conduit.main.mixins.api;

import net.minecraft.core.Position;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.UUID;

@Mixin(value = Entity.class, remap = false)
public abstract class EntityMixin implements systems.conduit.main.api.Entity {

    @Shadow public Level level;
    @Shadow public double x;
    @Shadow public double y;
    @Shadow public double z;

    @Shadow public abstract UUID getUUID();
    @Shadow public abstract void sendMessage(Component component);

    @Override
    public void sendMessage(String message) {
        sendMessage(new TextComponent(message));
    }

    @Shadow public abstract void teleportTo(double v, double v1, double v2);

    @Override
    public Level getLevel() {
        return level;
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public double getZ() {
        return this.z;
    }

    @Override
    public void teleport(systems.conduit.main.api.Entity entity) {
        this.teleport(entity.getX(), entity.getY(), entity.getZ());
    }

    @Override
    public void teleport(Position position) {
        this.teleport(position.x(), position.y(), position.z());
    }

    @Override
    public void teleport(double x, double y, double z) {
        this.teleportTo(x, y, z);
    }
}
