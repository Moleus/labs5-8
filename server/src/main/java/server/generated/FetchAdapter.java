package server.generated;

import model.data.Coordinates;
import model.data.Flat;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Returns an entity from a ResultRow
 */
public class FetchAdapter {
  public static Flat unwrap(ResultSet queryResult) throws SQLException {
    return Flat.builder()
        .id(queryResult.getLong("id"))
        .name(queryResult.getString("name"))
        .creationDate(queryResult.getTimestamp().toLocalDateTime())
        .coordinates(new Coordinates(queryResult.getDouble("cord_x"), queryResult.getInt("cord_y")))
        //...
        .build();
  }

}