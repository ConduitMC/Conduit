package systems.conduit.core.events.types

import net.minecraft.core.Direction
import net.minecraft.core.Location
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import systems.conduit.api.Level
import systems.conduit.api.Player
import systems.conduit.core.events.Cancellable

/**
 * @author Innectic
 * @since 10/21/2019
 */
class WorldEvents {

    class BlockPlaceEvent(val player: Player, val blockState: BlockState, val hand: InteractionHand, val clickedFace: Direction): Cancellable()

    class BlockInteractEvent(val player: Player, val blockState: BlockState, val hand: InteractionHand, val itemInHand: ItemStack, val clickedFace: Direction,
                             val sneaking: Boolean, val inside: Boolean): Cancellable()

    class BlockBreakEvent(val player: Player, val blockState: BlockState): Cancellable()

    class WorldSaveEvent(val level: ServerLevel): EventType()

    class ChunkLoadEvent(val level: Level, val chunk: ChunkPos): Cancellable()

    class ChunkUnloadEvent(val level: Level, val chunk: ChunkPos): Cancellable()

    class PortalCreateEvent: EventType() // TODO: Whatever goes in here

    class WeatherChangeEvent: Cancellable() // TODO: Whatever goes in here

    class LightningStrikeEvent(val location: Location): Cancellable()

    class ThunderStatusChangeEvent(val isThundering: Boolean): Cancellable()

    class GrowEvent(val block: Block): Cancellable()

    class BlockBurnEvent(val block: Block): Cancellable()

    class LeafDecayEvent(val block: Block): Cancellable()

    class BlockIgniteEvent(val block: Block): Cancellable() // TODO: Ignite source
}
