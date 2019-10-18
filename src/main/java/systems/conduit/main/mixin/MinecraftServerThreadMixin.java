package systems.conduit.main.mixin;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = "net/minecraft/server/dedicated/DedicatedServer$2")
public class MinecraftServerThreadMixin implements Runnable {

    @Override
    public void run() {
    }
}
