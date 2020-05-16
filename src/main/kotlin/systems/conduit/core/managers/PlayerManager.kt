package systems.conduit.core.managers

import net.minecraft.network.chat.TextComponent
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.dimension.DimensionType
import systems.conduit.api.MinecraftServer
import systems.conduit.api.Player
import systems.conduit.core.Conduit
import java.util.*
import java.util.function.Consumer

/*
 * @author Innectic
 * @since 10/26/2019
 */
// TODO: This needs to be moved to the API
class PlayerManager {
    /**
     * Find a player by name.
     *
     * @since API 0.1
     * @param name The name of the player
     * @return the player, if they're online. Empty otherwise.
     */
    @Deprecated("""Use {@link #getPlayer(UUID)} instead.
     
      """)
    fun getPlayer(name: String): Player? {
        // Check all levels for the player
        for ((_, value) in Conduit.server.getLevels()) {
            // Find all players who have matching names
            val players = value!!.getPlayers { s: ServerPlayer -> s.name.string == name }
            return if (players.size == 0) null else players[0] as Player

            // We'll assume that if there's any players in this list, that the first one should match the name. So, cast it to our type and return it.
        }
        return null
    }

    /**
     * Find a player by UUID.
     *
     * @since API 0.1
     *
     * @param uuid the uuid of the player to find.
     * @return the player, if they're online. Empty otherwise.
     */
    fun getPlayer(uuid: UUID): Player? {
        // Check all levels for the player
        for ((_, value) in Conduit.server.getLevels()) {
            // Find all players who have matching uuids
            val players = value!!.getPlayers { s: ServerPlayer -> s.uuid == uuid }
            return if (players.size == 0) null else players[0] as Player

            // We'll assume that if there's any players in this list, that the first one should match the name. So, cast it to our type and return it.
        }
        return null
    }

    /**
     * Find a player by UUID, in a specified world.
     *
     * @since API 0.1
     *
     * @param uuid the uuid of the player to find.
     * @param level the level that they should be in.
     * @return the player, if they're online and in the world. Empty otherwise.
     */
    fun getPlayer(uuid: UUID, level: DimensionType?): Player? {
        // Find all players who have the given name.
        val currentLevel = Conduit.server.getLevel(level) ?: return null
        val players = currentLevel.getPlayers { p: ServerPlayer -> p.uuid == uuid }
        return if (players.size == 0) null else players[0] as Player
        // We have a player that is most likely the one we want, so cast it to our and return it.
    }

    /**
     * Find a player by name in a given world.
     *
     * @since API 0.1
     * @param name the name of the player to find.
     * @param level the level the player should be in.
     * @return the player, if they're online and in the specified world. Empty otherwise.
     */
    @Deprecated("""Use {@link #getPlayer(UUID, DimensionType)} instead.
     
      """)
    fun getPlayerInWorld(name: String, level: DimensionType?): Player? {
        // Find all players who have the given name.
        val currentLevel = Conduit.server.getLevel(level) ?: return null
        val players = currentLevel.getPlayers { p: ServerPlayer -> p.name.string == name }
        return if (players.size == 0) null else players[0] as Player

        // We have a player that is most likely the one we want, so cast it to our and return it.
    }

    /**
     * Broadcast a message to all online players.
     *
     * @since API 0.1
     *
     * @param message the message to broadcast
     */
    fun broadcastMessage(message: String?) {
        this.broadcastMessage(TextComponent(message))
    }

    /**
     * Broadcast a message to all online players.
     *
     * @since API 0.1
     *
     * @param component the message to broadcast
     */
    fun broadcastMessage(component: TextComponent?) {
        Conduit.server.getLevels().values.forEach { level ->
            level?.players()?.forEach { player ->
                player.sendMessage(component)
            }
        }
    }
}
