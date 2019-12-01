package systems.conduit.main.api.factories;

import lombok.Builder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.Level;

import java.util.Optional;

/**
 * @author Innectic
 * @since 11/27/2019
 */
@Builder
public class EntityFactory {

    private TextComponent name;
    private EntityType type;
    private MobSpawnType spawnType;
    private Level level;
    private CompoundTag tag;
    private BlockPos position;
    private boolean center;
    private boolean negativeOffset;
    private Component component;

    public Optional<Entity> spawn() {
        if (level == null || position == null || type == null || spawnType == null) return Optional.empty();

        Entity entity = type.spawn(level, tag, component, null, position, spawnType, center, negativeOffset);
        if (entity == null) return Optional.empty();

        if (name != null) entity.setCustomName(name);
        return Optional.of(entity);
    }
}
