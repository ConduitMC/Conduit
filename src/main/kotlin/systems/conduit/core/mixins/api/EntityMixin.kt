package systems.conduit.core.mixins.api

import net.minecraft.core.Direction
import net.minecraft.core.Position
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.TextComponent
import net.minecraft.sounds.SoundEvent
import net.minecraft.tags.Tag
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Pose
import net.minecraft.world.level.Level
import net.minecraft.world.level.material.Fluid
import net.minecraft.world.phys.Vec3
import net.minecraft.world.scores.Team
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import systems.conduit.api.Entity
import java.util.*
import java.util.stream.Collectors


@Mixin(value = [net.minecraft.world.entity.Entity::class], remap = false)
abstract class EntityMixin: Entity {

    @Shadow
    override var level: Level? = null

    @Shadow
    override var x = 0.0

    @Shadow
    override var y = 0.0

    @Shadow
    override var z = 0.0

    @Shadow
    abstract override fun getUUID(): UUID?

    @Shadow
    abstract override fun sendMessage(message: Component?)
    override fun sendMessage(message: String?) {
        sendMessage(TextComponent(message))
    }

    @Shadow
    abstract fun teleportTo(v: Double, v1: Double, v2: Double)

    override fun teleport(entity: Entity?) {
        this.teleport(entity!!.x, entity.y, entity.z)
    }

    override fun teleport(position: Position?) {
        this.teleport(position!!.x(), position.y(), position.z())
    }

    override fun teleport(x: Double, y: Double, z: Double) {
        teleportTo(x, y, z)
    }

    @Shadow
    abstract override fun isSpectator(): Boolean

    @Shadow
    abstract override fun unRide()

    @Shadow
    abstract override fun getType(): EntityType<*>?

    @Shadow
    abstract override fun getId(): Int

    @Shadow
    abstract override fun setId(id: Int)

    @Shadow
    abstract override fun getTags(): Set<String?>?

    @Shadow
    abstract override fun addTag(tag: String?): Boolean

    @Shadow
    abstract override fun removeTag(tag: String?): Boolean

    @Shadow
    abstract override fun kill()

    @Shadow
    abstract override fun getPose(): Pose?

    @Shadow
    abstract override fun getPortalWaitTime(): Int

    @Shadow
    abstract override fun setSecondsOnFire(time: Int)

    @Shadow
    abstract override fun setRemainingFireTicks(ticks: Int)

    @Shadow
    abstract override fun getRemainingFireTicks(): Int

    @Shadow
    abstract override fun clearFire()

    @Shadow
    abstract override fun isSilent(): Boolean

    @Shadow
    abstract override fun setSilent(silent: Boolean)

    @Shadow
    abstract override fun isNoGravity(): Boolean

    @Shadow
    abstract override fun setNoGravity(gravity: Boolean)

    @Shadow
    abstract override fun isInWater(): Boolean

    @Shadow
    abstract override fun isInWaterOrRain(): Boolean

    @Shadow
    abstract override fun isInWaterRainOrBubble(): Boolean

    @Shadow
    abstract override fun isInWaterOrBubble(): Boolean

    @Shadow
    abstract override fun isUnderWater(): Boolean

    @Shadow
    abstract override fun isUnderLiquid(fluid: Tag<Fluid?>?): Boolean

    @Shadow
    abstract override fun setInLava()

    @Shadow
    abstract override fun isInLava(): Boolean

    @Shadow
    abstract override fun moveRelative(scale: Float, vector: Vec3?)

    @Shadow
    abstract override fun moveTo(x: Double, y: Double, z: Double, xRot: Float, yRot: Float)

    @Shadow
    abstract fun distanceTo(entity: net.minecraft.world.entity.Entity?): Float

    @Shadow
    abstract override fun distanceToSqr(x: Double, y: Double, z: Double): Double

    @Shadow
    abstract fun distanceToSqr(entity: net.minecraft.world.entity.Entity?): Double

    @Shadow
    abstract override fun distanceToSqr(vector: Vec3?): Double

    @Shadow
    abstract override fun getEyePosition(scale: Float): Vec3?

    @Shadow
    abstract override fun isAlive(): Boolean

    @Shadow
    abstract override fun isInWall(): Boolean

    @Shadow
    abstract fun startRiding(entity: net.minecraft.world.entity.Entity?): Boolean

    @Shadow
    abstract override fun stopRiding()

    @Shadow
    abstract override fun isSwimming(): Boolean

    @Shadow
    abstract override fun setSwimming(state: Boolean)

    @Shadow
    abstract override fun isSprinting(): Boolean

    @Shadow
    abstract override fun setSprinting(state: Boolean)

    @Shadow
    abstract override fun isGlowing(): Boolean

    @Shadow
    abstract override fun setGlowing(state: Boolean)

    @Shadow
    abstract override fun isInvisible(): Boolean

    @Shadow
    abstract fun `shadow$getTeam`(): Team?

    @Shadow
    abstract override fun isAlliedTo(team: Team?): Boolean

    @Shadow
    abstract fun isAlliedTo(entity: net.minecraft.world.entity.Entity?): Boolean

    @Shadow
    abstract override fun setInvisible(state: Boolean)

    @Shadow
    abstract override fun getMaxAirSupply(): Int

    @Shadow
    abstract override fun setAirSupply(supply: Int)

    @Shadow
    abstract override fun getDirection(): Direction?

    @Shadow
    abstract fun `shadow$getControllingPassenger`(): net.minecraft.world.entity.Entity?

    @Shadow
    abstract fun `shadow$getPassengers`(): List<net.minecraft.world.entity.Entity?>

    @Shadow
    abstract fun hasPassenger(entity: net.minecraft.world.entity.Entity?): Boolean

    @Shadow
    abstract override fun hasPassenger(entity: Class<out net.minecraft.world.entity.Entity?>?): Boolean

    @Shadow
    abstract override fun getIndirectPassengers(): Collection<net.minecraft.world.entity.Entity?>?

    @Shadow
    abstract override fun hasOnePlayerPassenger(): Boolean

    @Shadow
    abstract fun isPassengerOfSameVehicle(entity: net.minecraft.world.entity.Entity?): Boolean

    @Shadow
    abstract fun hasIndirectPassenger(entity: net.minecraft.world.entity.Entity?): Boolean

    @Shadow
    abstract override fun position(): Vec3?

    @Shadow
    protected abstract fun `shadow$getSwimSound`(): SoundEvent?

    @Shadow
    protected abstract fun `shadow$getSwimSplashSound`(): SoundEvent?

    @Shadow
    protected abstract fun `shadow$getSwimHighSpeedSplashSound`(): SoundEvent?

    override fun conduit_getTeam(): Team? {
        return `shadow$getTeam`()
    }

    override fun conduit_getSwimSound(): SoundEvent? {
        return `shadow$getSwimSound`()
    }

    override fun conduit_getSwimSplashSound(): SoundEvent? {
        return `shadow$getSwimSplashSound`()
    }

    override fun conduit_getSwimHighSpeedSplashSound(): SoundEvent? {
        return `shadow$getSwimHighSpeedSplashSound`()
    }

    override fun conduit_getPassengers(): List<Entity?>? {
        return `shadow$getPassengers`().stream().map { obj: net.minecraft.world.entity.Entity? -> Entity::class.java.cast(obj) }.collect(Collectors.toList())
    }

    override fun conduit_getControllingPassenger(): Entity? {
        val entity = `shadow$getControllingPassenger`() ?: return null
        return entity as Entity
    }
}
