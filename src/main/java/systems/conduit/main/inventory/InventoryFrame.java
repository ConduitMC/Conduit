package systems.conduit.main.inventory;

import lombok.Getter;
import net.minecraft.world.inventory.AbstractContainerMenu;
import systems.conduit.main.api.ServerPlayer;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Innectic
 * @since 12/12/2020
 */
public class InventoryFrame {

    private final Map<Integer, InventoryButton> buttons = new HashMap<>();
    private AbstractContainerMenu menu;

    @Getter private boolean changed = false;

    protected void prepareFrame() {
    }

    protected void showFrame(ServerPlayer player) {
    }

    protected void hideFrame(ServerPlayer player) {
    }

    protected void updateFrame(ServerPlayer player) {
    }
}
