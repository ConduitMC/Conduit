package systems.conduit.main.mixins;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "net/minecraft/server/dedicated/DedicatedServer$2")
public class MinecraftServerThreadMixin implements Runnable {

    /**
     * Kills default server input thread in favor of ours.
     *
     * @author ConduitMC
     */
    @Override
    public void run() {
    }
}
