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

    @Override
    public void set(String key, String value) {
        getSource().ifPresent(s -> {
            try {
                Connection conn = s.getConnection();

                PreparedStatement statement = conn.prepareStatement("INSERT INTO " + table + " (key,value) VALUES (?,?) ON DUPLICATE KEY UPDATE value=?");
                statement.setString(1, key);
                statement.setString(2, value);
                statement.setString(3, value);
                statement.execute();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void set(String key, int value) {
        set(key, String.valueOf(value));
    }

    @Override
    public void set(String key, float value) {
        set(key, String.valueOf(value));
    }

    @Override
    public void set(String key, double value) {
        set(key, String.valueOf(value));
    }

    @Override
    public void set(String key, Storable<?> value) {
        set(key, value.serialize());
    }

    @Override
    public Optional<Integer> getInt(String key) {
        return getString(key).map(v -> {
            try {
                return Integer.valueOf(v);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public Optional<Float> getFloat(String key) {
        return getString(key).map(v -> {
            try {
                return Float.valueOf(v);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public Optional<Double> getDouble(String key) {
        return getString(key).map(v -> {
            try {
                return Double.valueOf(v);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public Optional<String> getString(String key) {
        return getSource().map(s -> {
            try {
                Connection conn = s.getConnection();

                PreparedStatement statement = conn.prepareStatement("SELECT value FROM " + table + " WHERE key=?");
                statement.setString(1, key);
                ResultSet results = statement.executeQuery();

                if (results.next()) {
                    results.close();
                    statement.close();
                    return results.getString("value");
                }

                results.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public <T> Optional<Storable<T>> getCustom(String key) {
        return Optional.empty();  // TODO: Figure out how this is all going to work because it's confusing
    }

    @Override
    public void delete(String key) {
        getSource().ifPresent(s -> {
            try {
                Connection conn = s.getConnection();

                PreparedStatement statement = conn.prepareStatement("DELETE FROM " + table + " WHERE key=?");
                statement.setString(1, key);
                statement.execute();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
