package systems.conduit.main.api;

/**
 * Base Conduit interface for use with mixins.
 *
 * @since API 0.1
 *
 * Implementation: {@link systems.conduit.main.mixin.api.PlayerMixin}
 */
public interface Player extends LivingEntity {

    String getName();
}
