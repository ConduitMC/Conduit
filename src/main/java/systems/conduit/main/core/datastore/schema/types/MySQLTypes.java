package systems.conduit.main.core.datastore.schema.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Innectic
 * @since 12/25/2020
 */
@AllArgsConstructor
@Getter
public enum MySQLTypes {
    INVALID(null, null), STRING("text", String.class), DOUBLE("double", Double.class), LONG("bigint", Long.class), FLOAT("float", Float.class), CHAR("char", Character.class), BOOLEAN("tinyint(1)", Boolean.class);

    private String mysqlType;
    private Class<?> clazz;
}
