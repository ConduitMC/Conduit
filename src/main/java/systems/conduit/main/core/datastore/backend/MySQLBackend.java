package systems.conduit.main.core.datastore.backend;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import systems.conduit.main.core.datastore.Datastore;
import systems.conduit.main.core.datastore.schema.Schema;

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

    }

    @Override
    public void getAttachedSchema(Class<? extends Schema> schema) {

    }

    @Override
    public void insert(Class<? extends Schema> schema) {

    }

    @Override
    public void delete(Object primaryKey) {

    }

    @Override
    public List<Schema> filter(Class<? extends Schema> schema, Function<Schema, Boolean> filter) {
        return null;
    }

    private Optional<HikariDataSource> getSource() {
        return Optional.ofNullable(source);
    }
}
