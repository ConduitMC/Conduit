package systems.conduit.core.mixins.api

import com.mojang.authlib.GameProfile
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.TranslatableComponent
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.food.FoodData
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.InventoryMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.scores.Scoreboard
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Shadow
import systems.conduit.api.Player


@Mixin(value = [net.minecraft.world.entity.player.Player::class], remap = false)
abstract class PlayerMixin: Player {

    @Shadow
    var containerMenu: AbstractContainerMenu? = null

    @Shadow @Final
    var inventoryMenu: InventoryMenu? = null

    @Shadow
    private var respawnPosition: BlockPos? = null

    @Shadow
    private var respawnForced = false

    @get:Shadow
    abstract val gameProfile: GameProfile

    @Shadow
    protected abstract fun closeContainer()

    @Shadow
    abstract override fun getScoreboard(): Scoreboard?

    @Shadow
    abstract override fun isCreative(): Boolean

    @Shadow
    abstract override fun isSpectator(): Boolean

    @Shadow
    abstract override fun isSwimming(): Boolean

    @Shadow
    abstract override fun getArmorSlots(): Iterable<ItemStack?>?

    @Shadow
    abstract override fun getHandSlots(): Iterable<ItemStack?>?

    @Shadow
    abstract override fun addItem(item: ItemStack?): Boolean

    @Shadow
    abstract override fun setItemSlot(slot: EquipmentSlot?, item: ItemStack?)

    @Shadow
    abstract override fun isHurt(): Boolean

    @Shadow
    abstract override fun mayBuild(): Boolean

    @Shadow
    abstract override fun canEat(state: Boolean): Boolean

    @Shadow
    abstract override fun getFoodData(): FoodData?

    @Shadow
    abstract override fun causeFoodExhaustion(exhaustion: Float)

    @Shadow
    abstract override fun getXpNeededForNextLevel(): Int

    @Shadow
    abstract override fun giveExperienceLevels(levels: Int)

    @Shadow
    abstract override fun giveExperiencePoints(points: Int)

    @Shadow
    abstract override fun killed(entity: LivingEntity?)

    @Shadow
    abstract override fun isRespawnForced(): Boolean

    @Shadow
    abstract override fun getRespawnPosition(): BlockPos?

    @Shadow
    abstract override fun isSleepingLongEnough(): Boolean

    @Shadow
    abstract override fun stopSleeping()

    @Shadow
    abstract override fun getScore(): Int

    @Shadow
    abstract override fun setScore(score: Int)

    @Shadow
    abstract override fun increaseScore(score: Int)

    @Shadow
    protected abstract fun `shadow$getFireImmuneTicks`(): Int

    override fun conduit_getFireImmuneTicks(): Int {
        return `shadow$getFireImmuneTicks`()
    }

    @Shadow
    abstract override fun playSound(sound: SoundEvent?, volume: Float, pitch: Float)

    @Shadow
    abstract override fun getPortalWaitTime(): Int
    override fun setRespawnPosition(pos: BlockPos?, forced: Boolean) {
        if (pos != null) {
            if (pos != respawnPosition) {
                this.sendMessage(TranslatableComponent("block.minecraft.bed.set_spawn"))
            }
            respawnPosition = pos
            respawnForced = forced
        } else {
            respawnPosition = null
            respawnForced = false
        }
    }

    override fun closeOpenedContainer() {
        if (containerMenu !== inventoryMenu) {
            closeContainer()
        }
    }

    override fun getName(): String? {
        return gameProfile.name
    }
}
