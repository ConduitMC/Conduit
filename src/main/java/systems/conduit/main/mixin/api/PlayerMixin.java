package systems.conduit.main.mixin.api;

import org.spongepowered.asm.mixin.Mixin;
import systems.conduit.main.api.Player;

/**
 * @author Innectic
 * @since 10/11/2019
 */
@Mixin(value = net.minecraft.world.entity.player.Player.class, remap = false)
public abstract class PlayerMixin implements Player {
}
