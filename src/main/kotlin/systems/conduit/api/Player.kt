package systems.conduit.api

import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.food.FoodData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.scores.Scoreboard
import systems.conduit.api.inventory.ChestContainer


/**
 * Base Conduit interface for use with mixins.
 * Implementation: [systems.conduit.core.mixins.api.PlayerMixin]
 *
 * @since API 0.1
 */
interface Player: LivingEntity {
    fun getName(): String?
    fun closeOpenedContainer()
    fun openContainer(container: ChestContainer?)

    fun getScoreboard(): Scoreboard?
    fun isCreative(): Boolean
    override fun isSpectator(): Boolean
    override fun isSwimming(): Boolean

    fun getArmorSlots(): Iterable<ItemStack?>?
    fun getHandSlots(): Iterable<ItemStack?>?

    fun addItem(item: ItemStack?): Boolean
    fun setItemSlot(slot: EquipmentSlot?, item: ItemStack?)

    fun isHurt(): Boolean
    fun mayBuild(): Boolean
    fun canEat(state: Boolean): Boolean
    fun getFoodData(): FoodData?
    fun causeFoodExhaustion(exhaustion: Float)

    fun getXpNeededForNextLevel(): Int
    fun giveExperienceLevels(levels: Int)
    fun giveExperiencePoints(points: Int)

    fun killed(entity: net.minecraft.world.entity.LivingEntity?)
    fun setRespawnPosition(pos: BlockPos?, forced: Boolean)
    fun isRespawnForced(): Boolean
    fun getRespawnPosition(): BlockPos?
    fun isSleepingLongEnough(): Boolean
    fun stopSleeping()

    fun getScore(): Int
    fun setScore(score: Int)
    fun increaseScore(score: Int)
    fun conduit_getFireImmuneTicks(): Int

    fun playSound(sound: SoundEvent?, volume: Float, pitch: Float)
    override fun getPortalWaitTime(): Int
}
