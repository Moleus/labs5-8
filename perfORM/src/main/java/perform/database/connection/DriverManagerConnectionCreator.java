package perform.database.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * This connectionCreator implementation uses {@link DriverManager} to create connection.
 */
public class DriverManagerConnectionCreator extends BasicConnectionCreator {
  public DriverManagerConnectionCreator(String url, Properties connectionProps) {
    super(url, connectionProps);
  }

  @Override
  protected Connection makeConnection(String url, Properties info) {
    try {
      return DriverManager.getConnection(url, info);
    } catch (SQLException e) {
      throw convertSqlException("Error calling DriverManager.getConnection", e);
    }
  }
}
