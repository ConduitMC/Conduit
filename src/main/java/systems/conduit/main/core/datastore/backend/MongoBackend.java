package systems.conduit.main.core.datastore.backend;

import com.mongodb.*;
import systems.conduit.main.core.datastore.Datastore;
import systems.conduit.main.core.datastore.schema.Schema;
import systems.conduit.main.core.datastore.schema.utils.DatastoreUtils;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Innectic
 * @since 1/1/2021
 */
public class MongoBackend implements Datastore {

    private Optional<MongoClient> client = Optional.empty();

    private String database = "";

    @Override
    public void attach(Map<String, Object> meta) {
        if (!meta.containsKey("host") || !meta.containsKey("port") || !meta.containsKey("database") || !meta.containsKey("username") || !meta.containsKey("password") || !meta.containsKey("auth_db")) return;

        database = (String) meta.get("database");

        try {
            client = Optional.of(new MongoClient(new ServerAddress((String) meta.get("host"), (Integer) meta.get("port")),
                    Collections.singletonList(MongoCredential.createPlainCredential((String) meta.get("username"),
                            (String) meta.get("auth_db"), ((String) meta.get("password")).toCharArray()))));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void detach() {
        client.ifPresent(Mongo::close);
    }

    @Override
    public void attachSchema(Class<? extends Schema> schema) {
        // All we have to do here is make sure the collection exists for this schema.
        client.ifPresent(c -> {
            String name = DatastoreUtils.getNameOfSchema(schema);
            if (!c.getDB(database).collectionExists(name)) c.getDB(database).createCollection(name, new BasicDBObject());
        });
    }

    @Override
    public void insert(Schema schema) {
        //
    }

    @Override
    public void delete(Class<? extends Schema> schema, String key, Object value) {

    }

    @Override
    public void filterAndDelete(Class<? extends Schema> schema, Map<String, Object> filter) {

    }

    @Override
    public List<Schema> filter(Class<? extends Schema> schema, Map<String, Object> filter) {
        return null;
    }
}
