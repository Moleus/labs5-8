package server.generated;

import model.data.Flat;
import perform.exception.PerformException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Converts Entity into SQL statement and persists it in DB.
 */
public class SaveAdapter {
  public static void prepare(Flat entity, PreparedStatement ps) {
    try {
      //
      ps.setString(1, entity.getName());
      ps.setDouble(2, entity.getCoordinates().getX());
      ps.setInt(3, entity.getCoordinates().getY());
      ps.setString(4, entity.getView().toString());
      ps.setTimestamp(5, Timestamp.valueOf(""));
      // ...
      // set all except id
      if (ps.executeUpdate() == 0) throw new PerformException("No rows inserted");

      // TODO: DO I need manual commit, when I use only one table?
      // disable auto-commit before this.
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static long getGeneratedId(PreparedStatement ps) {
    try {
      ResultSet generatedKeys = ps.getGeneratedKeys();
      if (generatedKeys.next()) {
        return generatedKeys.getLong(1);
      } else {
        throw new PerformException("Failed to retreive generaeted Id");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      throw new PerformException("Failed to retreive generaeted Id");
    }
  }
}
