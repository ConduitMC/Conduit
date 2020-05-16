package systems.conduit.core.mixins

import net.minecraft.DetectedVersion
import org.spongepowered.asm.mixin.Final
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.Overwrite
import org.spongepowered.asm.mixin.Shadow

@Mixin(value = [DetectedVersion::class], remap = false)
abstract class VersionMixin {

    @Shadow @Final private val name: String? = null

    /**
     * Adds Conduit to the Minecraft version name.
     *
     * @author ConduitMC
     * @return Minecraft version and Conduit name
     */
    @Overwrite
    open fun getName(): String? {
        return "$name/Conduit"
    }
}
