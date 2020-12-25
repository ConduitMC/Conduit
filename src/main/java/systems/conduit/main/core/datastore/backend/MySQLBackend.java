package systems.conduit.main.core.datastore.backend;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import systems.conduit.main.core.datastore.DatastoreHandler;
import systems.conduit.main.core.datastore.Storable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

/**
 * Datastore backend that stores its information in MySQL / SQLite.
 *
 * @author Innectic
 * @since 12/31/2019
 */
public class MySQLBackend implements DatastoreHandler {

    private HikariConfig config = new HikariConfig();
    private HikariDataSource source;

    private String table;

    @Override
    public void attach(Map<String, Object> meta) {
        // TODO: Implement SQLite

        if (!meta.containsKey("host") || !meta.containsKey("port") || !meta.containsKey("database") || !meta.containsKey("username") || !meta.containsKey("password") || !meta.containsKey("table")) return;
        this.table = (String) meta.get("table");  // TODO: Make sure there's nothing scary in there

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

    private Optional<HikariDataSource> getSource() {
        return Optional.ofNullable(source);
    }
}
