package perform.database.connection;

import perform.exception.JDBCException;
import perform.exception.PerformException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Implements basic methods to handle db Connection.
 * Declares abstract factory method {@link #makeConnection} for specific connection types.
 */
public abstract class BasicConnectionCreator implements ConnectionCreator {

  private final String url;
  private final Properties connectionInfo;

  public BasicConnectionCreator(String url, Properties connectionProps) {
    this.url = url;
    this.connectionInfo = connectionProps;
  }

  @Override
  public String getUrl() {
    return this.url;
  }

  @Override
  public Connection createConnection() {
    Connection dbConnection = makeConnection(url, connectionInfo);
    if (dbConnection == null) {
      throw new PerformException("Unable to make JDBC connection [" + url + "]");
    }
    return dbConnection;
  }

  protected JDBCException convertSqlException(String message, SQLException cause) {
    return new JDBCException(message, cause);
  }

  /**
   * Implementation should create new connection from url and specific properties.
   */
  protected abstract Connection makeConnection(String url, Properties connectionProps);
}
