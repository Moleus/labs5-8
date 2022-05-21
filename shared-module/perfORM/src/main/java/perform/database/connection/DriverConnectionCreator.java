package perform.database.connection;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

/**
 * This connectionCreator implementation uses {@link Driver} to create connection.
 */
public class DriverConnectionCreator extends BasicConnectionCreator {
  private final Driver driver;

  public DriverConnectionCreator(Driver driver, String url, Properties connectionProps) {
    super(url, connectionProps);
    this.driver = driver;
  }

  @Override
  protected Connection makeConnection(String url, Properties connectionProps) {
    try {
      return driver.connect(url, connectionProps);
    } catch (SQLException e) {
      throw convertSqlException("Error calling Driver.connect", e);
    }
  }
}