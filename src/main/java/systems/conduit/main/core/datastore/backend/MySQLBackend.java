package systems.conduit.main.core.datastore.backend;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.datastore.Datastore;
import systems.conduit.main.core.datastore.schema.Schema;
import systems.conduit.main.core.datastore.schema.types.MySQLTypes;
import systems.conduit.main.core.datastore.schema.utils.DatabaseSchemaUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * Datastore backend that stores its information in MySQL / SQLite.
 *
 * @author Innectic
 * @since 12/31/2019
 */
public class MySQLBackend implements Datastore {

    private HikariConfig config = new HikariConfig();
    private HikariDataSource source;

    @Override
    public void attach(Map<String, Object> meta) {
        // TODO: Implement SQLite

        if (!meta.containsKey("host") || !meta.containsKey("port") || !meta.containsKey("database") || !meta.containsKey("username") || !meta.containsKey("password") || !meta.containsKey("table")) return;

        String url = "jdbc:mysql://" + meta.get("host") + ":"  + meta.get("port") + "/" + meta.get("database");
        config.setJdbcUrl(url);
        config.setUsername((String) meta.get("username"));
        config.setPassword((String) meta.get("password"));
        source = new HikariDataSource(config);
    }

    @Override
    public void detach() {
        this.source.close();
        this.source = null;
    }

    @Override
    public void attachSchema(String name, Class<? extends Schema> schema) {
        // First, convert the schema from the class to something mysql can underrstand
        Map<String, MySQLTypes> convertedSchema = DatabaseSchemaUtil.convertSchemaToMySQLSchema(schema);

        Optional<Connection> connection = getConnection();
        if (!connection.isPresent()) {
            Conduit.getLogger().error("Failed to get connection to mysql server");
            return;
        }

        StringBuilder statement = new StringBuilder("create table if not exists " + name + " (");

        // Build a statement to create the table based off of the converted schema.
        Iterator<Map.Entry<String, MySQLTypes>> schemaSet = convertedSchema.entrySet().iterator();
        boolean hasNext = schemaSet.hasNext();
        do {
            Map.Entry<String, MySQLTypes> entry = schemaSet.next();

            // Define the column
            statement
                    .append(entry.getKey())
                    .append(" ")
                    .append(entry.getValue().getMysqlType());

            // If there is another column after this one, then we need to add a comma.
            if (hasNext) statement.append(", ");
            hasNext = schemaSet.hasNext();
        } while (hasNext);

        statement.append(");");

        // Done building the table creation statement. Insert the new table.
        try {
            PreparedStatement creationStatement = connection.get().prepareStatement(statement.toString());
            creationStatement.execute();
        } catch (SQLException e) {
            Conduit.getLogger().error("Failed to attach new schema: " + name);
            e.printStackTrace();
        }
    }

    @Override
    public void insert(Schema schema) {

    }

    @Override
    public void delete(Object primaryKey) {

    }

    @Override
    public void filterAndDelete(Function<Schema, Boolean> filter) {
    }

    @Override
    public List<Schema> filter(Class<? extends Schema> schema, Function<Schema, Boolean> filter) {
        return null;
    }

    private Optional<HikariDataSource> getSource() {
        return Optional.ofNullable(source);
    }

    private Optional<Connection> getConnection() {
        return getSource().map(s -> {
            try {
                return s.getConnection();
            } catch (SQLException ignored) {
                return null;
            }
        });
    }
}
