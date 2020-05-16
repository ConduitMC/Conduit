package systems.conduit.api.factories

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.level.Level
import java.util.*

/**
 * Build new entities to add into the world.
 *
 * @author Innectic
 * @since 11/27/2019
 */
class EntityFactory {

    private val name: TextComponent? = null
    private val type: EntityType<*>? = null
    private val spawnType: MobSpawnType? = null
    private val level: Level? = null
    private val tag: CompoundTag? = null
    private val position: BlockPos? = null
    private val center = false
    private val negativeOffset = false
    private val component: Component? = null

    fun spawn(): Optional<Entity> {
        if (level == null || position == null || type == null || spawnType == null) return Optional.empty()
        val entity = type.spawn(level, tag, component, null, position, spawnType, center, negativeOffset) ?: return Optional.empty()
        if (name != null) entity.customName = name
        return Optional.of(entity)
    }
}
