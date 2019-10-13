package systems.conduit.main.mixin;

import net.minecraft.DetectedVersion;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = DetectedVersion.class, remap = false)
public abstract class VersionMixin {

    @Shadow @Final private String name;

    /**
     * @author Clutch
     */
    @Overwrite
    public String getName() {
        return name + "/Conduit";
    }
}
