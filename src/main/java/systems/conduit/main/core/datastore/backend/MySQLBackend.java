package systems.conduit.main.core.datastore.backend;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import systems.conduit.main.Conduit;
import systems.conduit.main.core.datastore.Datastore;
import systems.conduit.main.core.datastore.schema.Schema;
import systems.conduit.main.core.datastore.schema.types.MySQLTypes;
import systems.conduit.main.core.datastore.schema.utils.DatabaseSchemaUtil;
import systems.conduit.main.core.datastore.schema.utils.DatastoreUtils;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

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

        if (!meta.containsKey("host") || !meta.containsKey("port") || !meta.containsKey("database") || !meta.containsKey("username") || !meta.containsKey("password")) return;

        // Before doing anything, lets just make sure the database exists.
        String url = "jdbc:mysql://" + meta.get("host") + ":"  + meta.get("port");

        try {
            Connection connection = DriverManager.getConnection(url, meta.get("username").toString(), meta.get("password").toString());
            if (connection == null) {
                // Failed to connect to the given mysql server.
                Conduit.getLogger().error("Failed to connect to the mysql server on initial DB creation!");
                return;
            }
            PreparedStatement statement = connection.prepareStatement("create database if not exists " + meta.get("database"));
            statement.execute();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        url += "/" + meta.get("database");
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
    public void attachSchema(Class<? extends Schema> schema) {
        String name = DatastoreUtils.getNameOfSchema(schema);

        // First, convert the schema from the class to something mysql can understand
        Map<String, MySQLTypes> convertedSchema = DatabaseSchemaUtil.convertSchemaToMySQLSchema(schema);

        Optional<Connection> connection = getConnection();
        if (!connection.isPresent()) {
            Conduit.getLogger().error("Failed to get connection to mysql server");
            return;
        }

        StringBuilder statement = new StringBuilder("create table if not exists " + name + " (id mediumint not null auto_increment, ");

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
            if (schemaSet.hasNext()) statement.append(", ");
            hasNext = schemaSet.hasNext();
        } while (hasNext);

        statement.append(", primary key (id));");

        // Done building the table creation statement. Insert the new table.
        try {
            PreparedStatement creationStatement = connection.get().prepareStatement(statement.toString());
            creationStatement.execute();
            creationStatement.close();
            connection.get().close();
        } catch (SQLException e) {
            Conduit.getLogger().error("Failed to attach new schema: " + name);
            e.printStackTrace();
        }
    }

    @Override
    public void insert(Schema schema) {
        String name = DatastoreUtils.getNameOfSchema(schema.getClass());

        // First, convert the schema from the class to something mysql can understand
        Map<String, Object> serialized = schema.serialize();

        Optional<Connection> connection = getConnection();
        if (!connection.isPresent()) {
            Conduit.getLogger().error("Failed to get connection to mysql server");
            return;
        }

        StringBuilder statement = new StringBuilder("insert into " + name);

        StringBuilder columnNames = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");

        Iterator<Map.Entry<String, Object>> schemaSet = serialized.entrySet().iterator();
        boolean hasNext = schemaSet.hasNext();
        do {
            Map.Entry<String, Object> entry = schemaSet.next();

            // Insert into the column names and the associated values
            columnNames.append(entry.getKey());
            values.append(entry.getValue());

            // If there is another column after this one, then we need to add a comma.
            if (schemaSet.hasNext()) {
                columnNames.append(", ");
                values.append(", ");
            }
            hasNext = schemaSet.hasNext();
        } while (hasNext);

        statement.append(" ").append(columnNames).append(") values ").append(values).append(");");

        try {
            PreparedStatement insertStatement = connection.get().prepareStatement(statement.toString());
            insertStatement.execute();
            insertStatement.close();
            connection.get().close();
        } catch (SQLException e) {
            Conduit.getLogger().error("Failed to insert schema: " + name);
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Class<? extends Schema> schema, String key, Object value) {
        Map<String, Object> filter = new HashMap<>();
        filter.put(key, value);

        filterAndDelete(schema, filter);
    }

    @Override
    public void filterAndDelete(Class<? extends Schema> schema, Map<String, Object> filter) {
        Optional<Connection> connection = getConnection();
        if (!connection.isPresent()) {
            Conduit.getLogger().error("Failed to get connection to mysql server");
            return;
        }

        List<Schema> schemas = this.filter(schema, filter);
        if (schemas.size() == 0) return;
        String ids = "(" + schemas.stream().map(Schema::getId).map(String::valueOf).collect(Collectors.joining(",")) + ")";
        String query = "delete from " + DatastoreUtils.getNameOfSchema(schema) + " where id in " + ids;

        try {
            PreparedStatement statement = connection.get().prepareStatement(query);
            statement.execute();
            statement.close();
            connection.get().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Schema> filter(Class<? extends Schema> schema, Map<String, Object> filter) {
        Optional<Connection> connection = getConnection();
        if (!connection.isPresent()) {
            Conduit.getLogger().error("Failed to get connection to mysql server");
            return new ArrayList<>();
        }

        StringBuilder filterBuilder = new StringBuilder("select * from ").append(DatastoreUtils.getNameOfSchema(schema)).append(" where ");

        Iterator<Map.Entry<String, Object>> iterator = filter.entrySet().iterator();
        boolean hasNext = iterator.hasNext();

        do {
            Map.Entry<String, Object> entry = iterator.next();
            filterBuilder
                    .append(entry.getKey())
                    .append("=")
                    .append(entry.getValue());
            if (iterator.hasNext()) filterBuilder.append(", ");

            hasNext = iterator.hasNext();
        } while(hasNext);

        // Statement built, now get the results and start building out proper schemas.
        try {
            PreparedStatement statement = connection.get().prepareStatement(filterBuilder.toString());
            ResultSet results = statement.executeQuery();

            List<Schema> schemas = new ArrayList<>();

            while (results.next()) {
                Map<String, Object> data = new HashMap<>();

                int columns = results.getMetaData().getColumnCount();
                for (int i = 1; i <= columns; i++) data.put(results.getMetaData().getColumnName(i), results.getObject(i));

                // Now that we have pulled all the data out of this result, convert it into a schema.
                Schema.of(schema, data).ifPresent(schemas::add);
            }

            results.close();
            statement.close();
            connection.get().close();

            return schemas;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
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
