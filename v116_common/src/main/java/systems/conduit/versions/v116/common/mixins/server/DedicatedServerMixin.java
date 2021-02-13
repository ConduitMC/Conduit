package systems.conduit.versions.v116.common.mixins.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = net.minecraft.server.dedicated.DedicatedServer.class, remap = false)
public class DedicatedServerMixin {

    /**
     * Kills default server console jframe as is not compatible.
     *
     * @author ConduitMC
     */
    @Overwrite
    public void showGui() {
    }
}
