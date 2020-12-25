package systems.conduit.main;

import systems.conduit.main.core.datastore.Schema;
import systems.conduit.main.core.datastore.annotations.Field;

import java.util.UUID;

/**
 * @author Innectic
 * @since 12/24/2020
 */
public class TestSchema implements Schema {

    @Field("name") private String name;
    @Field(value = "uuid", factoryClazz = UUID.class, factoryMethod = "fromString") private UUID uuid;
    @Field("level") private int level;
}
