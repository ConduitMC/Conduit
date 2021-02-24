package systems.conduit.main.core.datastore.query;

import lombok.AllArgsConstructor;
import systems.conduit.main.core.datastore.schema.Schema;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Innectic
 * @since 12/24/2020
 */
@AllArgsConstructor
public class Result<T extends Schema> {

    private Map<String, Object> results;

    public static <T extends Schema>  Result<T> fromResultSet(ResultSet results) {
        Map<String, Object> newResult = new HashMap<>();

        return new Result<>(newResult);
    }
}
