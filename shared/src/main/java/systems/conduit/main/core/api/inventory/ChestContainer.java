package systems.conduit.main.core.api.inventory;

import lombok.Getter;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;

public class ChestContainer extends SimpleContainer {

    @Getter private MenuType<ChestMenu> type;
    @Getter private String title = "";

    private ChestContainer(int size) {
        super(size);
    }

    private static int getSizeFromEnum(MenuType<ChestMenu> size) {
        if (MenuType.GENERIC_9x2.equals(size)) return 18;
        else if (MenuType.GENERIC_9x3.equals(size)) return 27;
        else if (MenuType.GENERIC_9x4.equals(size)) return 36;
        else if (MenuType.GENERIC_9x5.equals(size)) return 45;
        else if (MenuType.GENERIC_9x6.equals(size)) return 54;
        return 9;
    }

    public static ChestContainer create(MenuType<ChestMenu> size, String title) {
        ChestContainer chestContainer = new ChestContainer(getSizeFromEnum(size));
        chestContainer.type = size;
        chestContainer.title = title;
        return chestContainer;
    }
}
