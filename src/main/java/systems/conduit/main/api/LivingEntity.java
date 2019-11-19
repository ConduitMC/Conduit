package systems.conduit.main.api;

/**
 * Base Conduit interface for use with mixins.
 * Implementation: {@link systems.conduit.main.mixins.api.LivingEntityMixin}
 *
 * @since API 0.1
 */
public interface LivingEntity extends Entity {

    float getHealth();
    void setHealth(float health);

    void setMaxHealth(float health);
    float getMaxHealth();

    boolean isSleeping();
}
