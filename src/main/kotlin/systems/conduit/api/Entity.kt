package systems.conduit.api

import net.minecraft.core.Direction
import net.minecraft.core.Position
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvent
import net.minecraft.tags.Tag
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Pose
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.phys.Vec3
import net.minecraft.world.scores.Team
import java.util.*


/**
 * Base Conduit interface for use with mixins.
 * Implementation: [systems.conduit.core.mixins.api.EntityMixin]
 *
 * @since API 0.1
 */
interface Entity {
    fun getUUID(): UUID?

    fun getX(): Double
    fun getY(): Double
    fun getZ(): Double

    fun sendMessage(message: String?)
    fun sendMessage(message: Component?)

    fun getLevel(): Level?

    fun teleport(x: Double, y: Double, z: Double)
    fun teleport(position: Position?)
    fun teleport(entity: Entity?)

    fun isSpectator(): Boolean
    fun unRide()
    fun getType(): EntityType<*>?
    fun getId(): Int
    fun setId(id: Int)
    fun getTags(): Set<String?>?
    fun addTag(tag: String?): Boolean
    fun removeTag(tag: String?): Boolean
    fun kill()
    fun getPose(): Pose?
    fun getPortalWaitTime(): Int
    fun setSecondsOnFire(time: Int)
    fun setRemainingFireTicks(ticks: Int)
    fun getRemainingFireTicks(): Int
    fun clearFire()
    fun isSilent(): Boolean
    fun setSilent(silent: Boolean)
    fun isNoGravity(): Boolean
    fun setNoGravity(gravity: Boolean)
    fun isInWater(): Boolean
    fun isInWaterOrRain(): Boolean
    fun isInWaterRainOrBubble(): Boolean
    fun isInWaterOrBubble(): Boolean
    fun isUnderWater(): Boolean
    fun isUnderLiquid(fluid: Tag<Fluid?>?): Boolean
    fun setInLava()
    fun isInLava(): Boolean
    fun moveRelative(scale: Float, vector: Vec3?)
    fun setLevel(level: Level?)
    fun moveTo(x: Double, y: Double, z: Double, xRot: Float, yRot: Float)
    fun distanceTo(entity: Entity?): Float
    fun distanceToSqr(x: Double, y: Double, z: Double): Double
    fun distanceToSqr(entity: Entity?): Double
    fun distanceToSqr(vector: Vec3?): Double
    fun getEyePosition(scale: Float): Vec3?
    fun isAlive(): Boolean
    fun isInWall(): Boolean
    fun startRiding(entity: Entity?): Boolean
    fun stopRiding()
    fun isSwimming(): Boolean
    fun setSwimming(state: Boolean)
    fun isSprinting(): Boolean
    fun setSprinting(state: Boolean)
    fun isGlowing(): Boolean
    fun setGlowing(state: Boolean)
    fun isInvisible(): Boolean
    fun conduit_getTeam(): Optional<Team?>?
    fun isAlliedTo(team: Team?): Boolean
    fun isAlliedTo(entity: Entity?): Boolean
    fun setInvisible(state: Boolean)
    fun getMaxAirSupply(): Int
    fun setAirSupply(supply: Int)
    fun getDirection(): Direction?
    fun conduit_getControllingPassenger(): Optional<Entity?>?
    fun conduit_getPassengers(): List<Entity?>?
    fun hasPassenger(entity: Entity?): Boolean
    fun hasPassenger(entity: Class<out net.minecraft.world.entity.Entity?>?): Boolean // TODO: Convert to Conduit entity

    fun getIndirectPassengers(): Collection<net.minecraft.world.entity.Entity?>? // TODO: Convert to Conduit entity

    fun hasOnePlayerPassenger(): Boolean
    fun isPassengerOfSameVehicle(entity: Entity?): Boolean
    fun hasIndirectPassenger(entity: Entity?): Boolean
    fun position(): Vec3?

    fun conduit_getSwimSound(): SoundEvent?
    fun conduit_getSwimSplashSound(): SoundEvent?
    fun conduit_getSwimHighSpeedSplashSound(): SoundEvent?
}
