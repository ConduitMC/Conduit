package systems.conduit.core.mixins

import org.spongepowered.asm.mixin.Mixin

@Mixin(targets = ["net/minecraft/server/dedicated/DedicatedServer$2"])
class MinecraftServerThreadMixin: Runnable {
    /**
     * Kills default server input thread in favor of ours.
     *
     * @author ConduitMC
     */
    override fun run() {}
}