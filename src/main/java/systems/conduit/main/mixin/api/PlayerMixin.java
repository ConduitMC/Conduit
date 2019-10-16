package systems.conduit.main.mixin.api;

import com.mojang.authlib.GameProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import systems.conduit.main.api.Player;

/**
 * @author Innectic
 * @since 10/11/2019
 */
@Mixin(value = net.minecraft.world.entity.player.Player.class, remap = false)
public abstract class PlayerMixin implements Player {

    @Shadow public abstract GameProfile getGameProfile();

    @Override
    public String getName() {
        return getGameProfile().getName();
    }
}
