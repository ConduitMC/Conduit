package systems.conduit.main.datastore;

/**
 * @author Innectic
 * @since 12/30/2019
 */
public interface Storable<T> {

    String serialize();
    T deserialize();
}
