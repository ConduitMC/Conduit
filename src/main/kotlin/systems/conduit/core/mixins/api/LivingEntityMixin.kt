package systems.conduit.core.mixins.apiimport

import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.MobType
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import org.spongepowered.asm.mixin.Final
import systems.conduit.api.LivingEntity
import systems.conduit.api.Player
import java.util.*

net.minecraft.world.item.enchantment.Enchantmentimport net.minecraft.nbt.CompoundTagimport net.minecraft.world.item.ItemStackimport java.lang.UnsupportedOperationExceptionimport net.minecraft.world.entity.EntityTypeimport net.minecraft.world.entity.MobSpawnTypeimport net.minecraft.core.BlockPosimport net.minecraft.world.SimpleContainerimport net.minecraft.world.inventory.MenuTypeimport net.minecraft.world.inventory.ChestMenuimport systems.conduit.api.inventory.ChestContainerimport net.minecraft.world.entity.MobCategoryimport net.minecraft.world.level.biome.Biome.SpawnerDataimport net.minecraft.world.level.biome.Biome.Precipitationimport net.minecraft.world.level.LevelReaderimport net.minecraft.world.level.biome.Biome.BiomeCategoryimport net.minecraft.world.level.block.state.BlockStateimport net.minecraft.world.level.chunk.LevelChunkimport net.minecraft.world.level.LightLayerimport net.minecraft.world.level.material.FluidStateimport net.minecraft.sounds.SoundEventimport net.minecraft.sounds.SoundSourceimport net.minecraft.world.level.block.entity.BlockEntityimport net.minecraft.world.level.LevelTypeimport net.minecraft.world.level.chunk.ChunkSourceimport net.minecraft.world.level.storage.LevelDataimport net.minecraft.world.level.GameRulesimport net.minecraft.world.DifficultyInstanceimport net.minecraft.world.level.border.WorldBorderimport java.util.UUIDimport net.minecraft.world.entity.Poseimport net.minecraft.world.phys.Vec3import net.minecraft.world.scores.Teamimport net.minecraft.world.scores.Scoreboardimport net.minecraft.world.entity.EquipmentSlotimport net.minecraft.world.food.FoodDataimport net.minecraft.world.effect.MobEffectInstanceimport net.minecraft.world.effect.MobEffectimport net.minecraft.world.entity.MobTypeimport net.minecraft.world.InteractionHandimport net.minecraft.world.level.material.Materialimport net.minecraft.world.level.dimension.DimensionTypeimport net.minecraft.server.level.ServerLevelimport net.minecraft.world.level.storage.LevelStorageSourceimport net.minecraft.util.profiling.GameProfilerimport net.minecraft.commands.CommandSourceStackimport net.minecraft.server.bossevents.CustomBossEventsimport net.minecraft.world.level.ChunkPosimport net.minecraft.world.entity.animal.Sheepimport net.minecraft.world.entity.monster.Slimeimport net.minecraft.world.entity.animal.Animalimport net.minecraft.world.level.block.SpawnerBlockimport systems.conduit.core.events.types.PlayerEvents.LeaveTypeimport systems.conduit.core.events.types.PlayerEvents.DamageMetaimport net.minecraft.world.entity.projectile.AbstractArrowimport net.minecraft.world.level.GameTypeimport java.lang.NoSuchMethodExceptionimport java.lang.IllegalAccessExceptionimport java.lang.InstantiationExceptionimport java.lang.reflect.InvocationTargetExceptionimport systems.conduit.core.events.EventManagerimport systems.conduit.core.events.EventTypeRegistryimport java.util.HashMapimport systems.conduit.core.events.types.WorldEvents.BlockInteractEventimport systems.conduit.core.events.types.WorldEvents.BlockBreakEventimport systems.conduit.core.events.types.WorldEvents.BlockPlaceEventimport systems.conduit.core.events.types.WorldEvents.WorldSaveEventimport systems.conduit.core.events.types.PlayerEvents.PlayerJoinEventimport systems.conduit.core.events.types.PlayerEvents.PlayerLeaveEventimport systems.conduit.core.events.types.PlayerEvents.PlayerDamageByEntityEventimport systems.conduit.core.events.types.PlayerEvents.PlayerDamageByPlayerEventimport systems.conduit.core.events.types.PlayerEvents.PlayerDamageByArrowEventimport systems.conduit.core.events.types.PlayerEvents.PlayerGameModeChangeEventimport systems.conduit.core.events.types.PlayerEvents.PlayerChatEventimport systems.conduit.core.events.types.PlayerEvents.PlayerCommandEventimport systems.conduit.core.events.types.PlayerEvents.ConsumeEventimport systems.conduit.core.events.types.EntityEvents.SheepGrowWoolEventimport systems.conduit.core.events.types.EntityEvents.BabySheepEatEventimport systems.conduit.core.events.types.EntityEvents.SlimeSplitEventimport systems.conduit.core.events.types.EntityEvents.EffectAddedToEntityEventimport systems.conduit.core.events.types.EntityEvents.EffectRemovedFromEntityEventimport systems.conduit.core.events.types.ServerEvents.ServerInitializedEventimport systems.conduit.core.events.types.ServerEvents.ServerStartingEventimport systems.conduit.core.events.types.ServerEvents.ServerShuttingDownEventimport systems.conduit.core.events.types.ServerEvents.PluginReloadEventimport org.spongepowered.asm.mixin.Mixinimport org.spongepowered.asm.mixin.Shadowimport java.util.stream.Collectorsimport net.minecraft.world.inventory.AbstractContainerMenuimport net.minecraft.world.inventory.InventoryMenuimport com.mojang.authlib.GameProfileimport net.minecraft.network.chat.TranslatableComponentimport net.minecraft.world.entity.ai.attributes.AttributeInstanceimport net.minecraft.world.entity.monster.SharedMonsterAttributesimport org.spongepowered.asm.mixin.injection.Injectimport org.spongepowered.asm.mixin.injection.Atimport org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnableimport org.spongepowered.asm.mixin.injection.callback.CallbackInfoimport net.minecraft.server.network.ServerGamePacketListenerImplimport net.minecraft.network.protocol.game.ClientboundOpenScreenPacketimport net.minecraft.network.protocol.game.ClientboundContainerSetContentPacketimport org.spongepowered.asm.mixin.injection.ModifyVariableimport net.minecraft.world.entity.ai.goal.BreedGoalimport systems.conduit.core.events.types.EntityEvents.EntityBreedEventimport systems.conduit.core.Conduitimport org.spongepowered.asm.mixin.Overwriteimport net.minecraft.world.entity.Mobimport net.minecraft.server.players.PlayerListimport net.minecraft.world.damagesource.DamageSourceimport net.minecraft.world.damagesource.EntityDamageSourceimport net.minecraft.server.level.ServerPlayerGameModeimport net.minecraft.world.item.UseOnContextimport net.minecraft.world.InteractionResultimport net.minecraft.server.dedicated.DedicatedServerimport com.mojang.datafixers.DataFixerimport com.mojang.authlib.yggdrasil.YggdrasilAuthenticationServiceimport com.mojang.authlib.minecraft.MinecraftSessionServiceimport com.mojang.authlib.GameProfileRepositoryimport net.minecraft.server.players.GameProfileCacheimport net.minecraft.server.level.progress.ChunkProgressListenerFactoryimport java.lang.Runnableimport net.minecraft.DetectedVersionimport net.minecraft.commands.CommandSourceimport net.minecraft.ChatFormattingimport org.spongepowered.asm.mixin.injection.ModifyArgimport systems.conduit.core.plugin.config.ConfigurationLoaderimport systems.conduit.core.plugin.config.loader.JsonLoaderimport java.io.FileReaderimport java.io.IOExceptionimport com.google.gson.Gsonimport com.google.gson.annotations.SerializedNameimport java.lang.NullPointerExceptionimport systems.conduit.core.plugin.config.defaults.DefaultConfigurationHandlerimport java.nio.file.Pathsimport com.google.gson.JsonIOExceptionimport systems.conduit.core.plugin.config.ConfigurationTypesimport systems.conduit.core.plugin.annotation .DependencyTypeimport kotlin.reflect.KClassimport systems.conduit.core.plugin.config.NoConfigimport systems.conduit.core.plugin.annotation .PluginMetaimport systems.conduit.core.plugin.PluginStateimport java.util.concurrent.ConcurrentHashMapimport systems.conduit.core.datastore.DatastoreControllerimport java.lang.SafeVarargsimport java.lang.ClassCastExceptionimport systems.conduit.core.commands.BaseCommandimport java.util.concurrent.ConcurrentLinkedQueueimport java.util.concurrent.atomic.AtomicReferenceimport java.net.URLClassLoaderimport java.util.jar.JarFileimport org.reflections.Reflectionsimport kotlin.jvm.Throwsimport java.lang.ClassNotFoundExceptionimport java.security.CodeSignerimport java.security.CodeSourceimport org.jline.terminal.TerminalBuilderimport org.jline.reader.LineReaderBuilderimport org.jline.reader.EndOfFileExceptionimport net.minecraft.DefaultUncaughtExceptionHandlerimport org.apache.logging.log4j.message.StringFormattedMessageimport systems.conduit.core.console.ConsoleColorUtilimport com.mojang.brigadier.builder.LiteralArgumentBuilderimport systems.conduit.core.commands.PluginsCommandimport systems.conduit.core.commands.VersionCommandimport com.mojang.brigadier.context.CommandContextimport java.util.function.BinaryOperatorimport com.mojang.brigadier.arguments.StringArgumentTypeimport net.minecraft.SharedConstantsimport net.minecraft.resources.ResourceLocationimport net.minecraft.world.level.LevelSettingsimport net.minecraft.server.level.progress.ChunkProgressListenerimport net.minecraft.world.level.chunk.ChunkStatusimport net.minecraft.server.bossevents.CustomBossEventimport systems.conduit.core.datastore.DatastoreHandlerimport com.zaxxer.hikari.HikariConfigimport com.zaxxer.hikari.HikariDataSourceimport java.sql.PreparedStatementimport java.sql.SQLExceptionimport systems.conduit.core.datastore.Storableimport java.lang.NumberFormatExceptionimport java.sql.ResultSetimport systems.conduit.core.datastore.ExpirableBackendimport redis.clients.jedis.Jedisimport systems.conduit.core.datastore.backend.MySQLBackendimport systems.conduit.core.datastore.backend.MemoryBackendimport java.util.function.IntPredicateimport systems.conduit.core.datastore.DatastoreBackendimport systems.conduit.core.commands.CommandManagerimport systems.conduit.core.managers.PlayerManagerimport systems.conduit.core.managers.LevelManagerimport systems.conduit.core.managers.BossBarManagerimport systems.conduit.core.ConduitConfigurationimport java.io.PrintStreamimport java.nio.file.StandardCopyOptionimport systems.conduit.core.ConduitConfiguration.MySQLConfigurationimport systems.conduit.core.ConduitConfiguration.RedisConfigurationimport systems.conduit.core.ConduitConfiguration.DatastoreConfiguration
@Mixin(value = [net.minecraft.world.entity.LivingEntity::class])
abstract class LivingEntityMixin: LivingEntity {
    @get:Shadow
    @set:Shadow
    abstract override var health: Float

    @get:Final
    @get:Shadow
    abstract override var maxHealth: Float
        set(health) {
            getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health.toDouble())
        }

    @Shadow
    abstract fun getAttribute(attribute: Attribute?): AttributeInstance

    @get:Shadow
    abstract override val isSleeping: Boolean

    @Shadow
    abstract override fun kill()

    @Shadow
    abstract override fun canBreatheUnderwater(): Boolean

    @get:Shadow
    abstract override val isBaby: Boolean

    @get:Shadow
    abstract override val scale: Float

    @Shadow
    abstract override fun rideableUnderWater(): Boolean

    @Shadow
    abstract fun `shadow$getLastHurtByMob`(): net.minecraft.world.entity.LivingEntity?

    @get:Shadow
    abstract override val lastHurtByMobTimestamp: Int

    @Shadow
    abstract fun `shadow$setLastHurtByMob`(entity: net.minecraft.world.entity.LivingEntity?)

    @Shadow
    protected abstract fun `shadow$removeEffectParticles`()

    @Shadow
    abstract override fun removeAllEffects(): Boolean

    @get:Shadow
    abstract override val activeEffects: Collection<MobEffectInstance?>?

    @get:Shadow
    abstract override val activeEffectsMap: Map<MobEffect?, MobEffectInstance?>?

    @Shadow
    abstract fun hasEffect(effect: MobEffect?): Boolean

    @Shadow
    abstract fun addEffect(effect: MobEffectInstance?): Boolean

    @Shadow
    abstract fun removeEffect(effect: MobEffect?): Boolean

    @get:Shadow
    abstract override val isInvertedHealAndHarm: Boolean

    @Shadow
    abstract override fun heal(amount: Float)

    @Shadow
    abstract fun `shadow$getKillCredit`(): net.minecraft.world.entity.LivingEntity?

    @get:Shadow
    @set:Shadow
    abstract override var arrowCount: Int

    @get:Shadow
    abstract override val mobType: MobType?

    @get:Shadow
    abstract override val mainHandItem: ItemStack?

    @get:Shadow
    abstract override val offhandItem: ItemStack?

    @Shadow
    abstract fun getItemInHand(hand: InteractionHand?): ItemStack?

    @Shadow
    abstract fun setItemInHand(hand: InteractionHand?, item: ItemStack?)

    @Shadow
    abstract fun hasItemInSlot(slot: EquipmentSlot?): Boolean

    @Shadow
    abstract override fun setSprinting(isSprinting: Boolean)

    @get:Shadow
    @set:Shadow
    abstract override var speed: Float

    @get:Shadow
    abstract override val isInWall: Boolean
    override fun conduit_getKillCredit(): Optional<LivingEntity?> {
        val entity = `shadow$getKillCredit`() ?: return Optional.empty()
        return Optional.of(entity as LivingEntity)
    }

    override fun conduit_removeEffectParticles() {
        `shadow$removeEffectParticles`()
    }

    override fun conduit_getLastHurtByMob(): Optional<LivingEntity?> {
        val entity = `shadow$getLastHurtByMob`() ?: return Optional.empty()
        return Optional.of(entity as LivingEntity)
    }

    override fun conduit_setLastHurtByMob(entity: LivingEntity?) {
        if (entity == null) return
        `shadow$setLastHurtByMob`(entity as net.minecraft.world.entity.LivingEntity?)
    }

    @Inject(method = ["eat"], at = [At("HEAD")])
    fun eat(level: Level?, itemStack: ItemStack?, cir: CallbackInfoReturnable<ItemStack?>?) {
        if (this is Player) {
            val event = ConsumeEvent(this as Player, itemStack)
            Conduit.getEventManager().dispatchEvent(event)
        }
    }

    @Inject(method = ["onEffectAdded"], at = [At("HEAD")])
    fun onEffectAdded(effect: MobEffectInstance?, ci: CallbackInfo?) {
        val event = EffectAddedToEntityEvent(this, effect)
        Conduit.getEventManager().dispatchEvent(event)
    }

    @Inject(method = ["onEffectRemoved"], at = [At("HEAD")])
    fun onEffectRemoved(effect: MobEffectInstance?, ci: CallbackInfo?) {
        val event = EffectRemovedFromEntityEvent(this, effect)
        Conduit.getEventManager().dispatchEvent(event)
    }
}