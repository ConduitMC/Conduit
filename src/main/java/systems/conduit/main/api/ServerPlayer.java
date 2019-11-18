package systems.conduit.main.api;

import systems.conduit.main.inventory.ChestContainer;

public interface ServerPlayer extends Player {

    void openContainer(ChestContainer container);

}
