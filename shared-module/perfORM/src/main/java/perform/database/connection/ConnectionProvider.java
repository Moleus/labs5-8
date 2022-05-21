package perform.database.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {
  Connection getConnection();

  void closeConnection() throws SQLException;
}
