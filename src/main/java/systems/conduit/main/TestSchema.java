package systems.conduit.main;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import systems.conduit.main.core.datastore.schema.Schema;
import systems.conduit.main.core.datastore.schema.annotations.Field;

import java.util.UUID;

/**
 * @author Innectic
 * @since 12/24/2020
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TestSchema extends Schema {

    @Field("name") private String name;
    @Field(value = "uuid", factoryClazz = UUID.class, factoryMethod = "fromString") private UUID uuid;
    @Field("level") private int level;
}
