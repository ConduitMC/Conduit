package systems.conduit.main.mixins.api;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import systems.conduit.main.api.Player;

@Mixin(value = net.minecraft.world.entity.player.Player.class, remap = false)
public abstract class PlayerMixin implements Player {

    @Shadow public abstract GameProfile getGameProfile();

    @Shadow public AbstractContainerMenu containerMenu;
    @Shadow @Final public InventoryMenu inventoryMenu;
    @Shadow protected abstract void closeContainer();

    public void closeOpenedContainer() {
        if (this.containerMenu != this.inventoryMenu) {
            this.closeContainer();
        }
    }

    @Override
    public String getName() {
        return getGameProfile().getName();
    }
}
