package systems.conduit.main.api;

import systems.conduit.main.inventory.ChestContainer;

/**
 * Base Conduit interface for use with mixins.
 *
 * @since API 0.1
 *
 * Implementation: {@link systems.conduit.main.mixins.api.PlayerMixin}
 */
public interface Player extends LivingEntity {

    String getName();
    void closeOpenedContainer();
    void openContainer(ChestContainer container);



}
