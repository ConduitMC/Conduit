package systems.conduit.main.inventory;

import lombok.Getter;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import systems.conduit.main.api.factories.InventoryFactory;

public class ChestContainer extends SimpleContainer {

    @Getter private MenuType<ChestMenu> type;
    @Getter private String title = "";

    private ChestContainer(int size) {
        super(size);
    }

    public static ChestContainer create(MenuType<ChestMenu> size, String title) {
        ChestContainer chestContainer = new ChestContainer(InventoryFactory.getSizeFromEnum(size));
        chestContainer.type = size;
        chestContainer.title = title;
        return chestContainer;
    }
}
