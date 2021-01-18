package systems.conduit.main.core.datastore.backend;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import systems.conduit.main.core.datastore.Datastore;
import systems.conduit.main.core.datastore.schema.Schema;
import systems.conduit.main.core.datastore.schema.utils.DatastoreUtils;

import java.util.*;
import java.util.function.Consumer;

/**
 * @author Innectic
 * @since 1/1/2021
 */
public class MongoBackend implements Datastore {

    private Optional<MongoClient> client = Optional.empty();
    private MongoClientOptions options;

    private String databaseName = "";
    private Optional<MongoDatabase> database;

    @Override
    public void attach(Map<String, Object> meta) {
        if (!meta.containsKey("host") || !meta.containsKey("port") || !meta.containsKey("database") || !meta.containsKey("username") || !meta.containsKey("password") || !meta.containsKey("auth_db")) return;

        databaseName = (String) meta.get("database");

        MongoCredential creds = MongoCredential.createCredential((String) meta.get("username"), (String) meta.get("auth_db"), ((String) meta.get("password")).toCharArray());

        CodecRegistry pojoRegistry = CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(), pojoRegistry);
        options = MongoClientOptions.builder()
                .codecRegistry(codecRegistry)
                .build();

        client = Optional.of(new MongoClient(new ServerAddress((String) meta.get("host"), (Integer) meta.get("port")), creds, options));

        database = client.map(c -> c.getDatabase(databaseName));
    }

    @Override
    public void detach() {
        client.ifPresent(Mongo::close);
    }

    @Override
    public void attachSchema(Class<? extends Schema> schema) {
        // All we have to do here is make sure the collection exists for this schema.
        database.ifPresent(d -> {
            String name = DatastoreUtils.getNameOfSchema(schema);
            if (d.getCollection(name) == null) d.createCollection(name);
        });
    }

    @Override
    public void insert(Schema schema) {
        Class<Schema> schemaClass = (Class<Schema>) schema.getClass();

        database.ifPresent(d -> {
            String name = DatastoreUtils.getNameOfSchema(schema.getClass());
            MongoCollection<Schema> collection = d.getCollection(name, schemaClass);
            if (collection == null) return;

            collection.insertOne(schema);
        });
    }

    @Override
    public void delete(Class<? extends Schema> schema, String key, Object value) {
        Map<String, Object> filter = new HashMap<>();
        filter.put(key, value);
        filterAndDelete(schema, filter);
    }

    @Override
    public void filterAndDelete(Class<? extends Schema> schema, Map<String, Object> filter) {
        database.ifPresent(d -> {
            String name = DatastoreUtils.getNameOfSchema(schema);
            Class<Schema> schemaClass = (Class<Schema>) schema;

            MongoCollection<Schema> collection = d.getCollection(name, schemaClass);
            if (collection == null) return;

            Document filterDocument = new Document();
            filter.forEach(filterDocument::put);

            collection.deleteOne(filterDocument);
        });
    }

    @Override
    public List<Schema> filter(Class<? extends Schema> schema, Map<String, Object> filter) {
        Class<Schema> schemaClass = (Class<Schema>) schema;
        List<Schema> schemas = new ArrayList<>();

        database.ifPresent(d -> {
            String name = DatastoreUtils.getNameOfSchema(schema);
            MongoCollection<Schema> collection = d.getCollection(name, schemaClass);
            if (collection == null) return;

            Document filterDocument = new Document();
            filter.forEach(filterDocument::put);
            collection.find(filterDocument).forEach((Consumer<? super Schema>) schemas::add);
        });
        return schemas;
    }
}
