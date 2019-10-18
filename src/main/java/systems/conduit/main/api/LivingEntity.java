package systems.conduit.main.api;

public interface LivingEntity extends Entity {

    float getHealth();
    void setHealth(float health);

    void setMaxHealth(float health);
    float getMaxHealth();
}
