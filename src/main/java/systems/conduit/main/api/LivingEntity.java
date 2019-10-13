package systems.conduit.main.api;

/**
 * @author Innectic
 * @since 10/12/2019
 */
public interface LivingEntity extends Entity {

    float getHealth();
    void setHealth(float health);

    void setMaxHealth(float health);
    float getMaxHealth();
}
