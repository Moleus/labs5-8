package perform.database.connection;

import java.sql.Connection;

public interface ConnectionCreator {
  /**
   * For testing purposes only.
   */
  String getUrl();

  /**
   * Returns connection to DB. Details depend on implementation.
   */
  Connection createConnection();
}
