package systems.conduit.main.api.factories;

import lombok.Builder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;

import java.util.Optional;

/**
 * Build new entities to add into the world.
 *
 * @author Innectic
 * @since 11/27/2019
 */
@Builder
public class EntityFactory {

    private TextComponent name;
    @Builder.Default private EntityType type = EntityType.COW;
    @Builder.Default private MobSpawnType spawnType = MobSpawnType.SPAWN_EGG;
    private ServerLevel level;
    private CompoundTag entityTag;
    private BlockPos position;
    private boolean center;
    private boolean negativeOffset;
    private Component customName;

    public Optional<Entity> spawn() {
        if (level == null || position == null || type == null || spawnType == null) return Optional.empty();

        Entity entity = type.spawn(level, entityTag, customName, null, position, spawnType, center, negativeOffset);

        if (entity == null) return Optional.empty();
        if (name != null) entity.setCustomName(name);

        entity.teleportTo(position.getX(), position.getY(), position.getZ());

        return Optional.of(entity);
    }
}
