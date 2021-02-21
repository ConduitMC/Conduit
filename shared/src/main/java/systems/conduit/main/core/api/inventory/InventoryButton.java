package systems.conduit.main.core.api.inventory;

import lombok.*;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import systems.conduit.main.core.api.mixins.ServerPlayer;

import java.util.function.BiConsumer;

/**
 * @author Innectic
 * @since 12/11/2020
 */
@AllArgsConstructor
public class InventoryButton {
    @Setter(AccessLevel.PACKAGE) @Getter(AccessLevel.PACKAGE) @NonNull private ItemStack item;
    @Getter(AccessLevel.PACKAGE) private BiConsumer<ServerPlayer, ClickType> consumer;
}
