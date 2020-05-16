package systems.conduit.core.managers

import net.minecraft.network.chat.TextComponent
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.bossevents.CustomBossEvent
import systems.conduit.core.Conduit
import java.util.*

// TODO: This needs to be moved to the API
class BossBarManager {
    fun createBossBar(id: String?): CustomBossEvent? {
        return Conduit.server.customBossEvents.create(ResourceLocation(id), TextComponent(""))
    }

    operator fun get(id: String?): CustomBossEvent? {
        return Conduit.server.customBossEvents[ResourceLocation(id)]
    }

    fun remove(bossBar: CustomBossEvent?) {
        Conduit.server.customBossEvents.remove(bossBar)
    }
}
