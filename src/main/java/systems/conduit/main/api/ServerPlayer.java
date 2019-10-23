package systems.conduit.main.api;

import net.minecraft.world.SimpleContainer;

public interface ServerPlayer extends Player {

    void openContainer(SimpleContainer container, String title);

}
