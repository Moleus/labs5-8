package perform.database.connection;

import lombok.extern.log4j.Log4j2;
import perform.LogHelper;
import perform.cfg.AvailableSettings;
import perform.exception.ClassLoadException;
import perform.exception.PerformException;
import perform.service.Configurable;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

@Log4j2
public class DriverManagerConnectionProvider implements ConnectionProvider, Configurable {
  ConnectionCreator connectionCreator;
  Connection currentConnection;

  @Override
  public void configure(Properties configurationValues) {
    connectionCreator = buildCreator(configurationValues);
  }

  @Override
  public Connection getConnection() {
    if (connectionCreator == null) {
      throw new PerformException("Connection provider should be configured before asking for connection");
    }
    if (null == currentConnection) {
      currentConnection = connectionCreator.createConnection();
    }
    return currentConnection;
  }

  @Override
  public void closeConnection() throws SQLException {
    if (null == currentConnection) {
      throw new PerformException("No connection was initialized, nothing to close");
    }
    currentConnection.close();
  }

  private ConnectionCreator buildCreator(Properties configurationValues) {
    String url = configurationValues.getProperty(AvailableSettings.URL);
    if (url == null) {
      String errorMessage = LogHelper.jdbcUrlNotSpecified(AvailableSettings.URL);
      log.error(errorMessage);
      throw new PerformException(errorMessage);
    }
    Driver driver = tryToLoadDriver(configurationValues.getProperty(AvailableSettings.DRIVER));
    if (driver != null) {
      return new DriverConnectionCreator(driver, url, configurationValues);
    }
    return new DriverManagerConnectionCreator(url, configurationValues);
  }

  private Driver tryToLoadDriver(String driverClassName) {
    if (driverClassName == null) {
      log.debug("No Driver class name specified");
      return null;
    }
    try {
      @SuppressWarnings("unchecked")
      Class<Driver> driverClass = (Class<Driver>) Class.forName(driverClassName);
      return driverClass.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      log.error("Failed to load Driver class for JDBC from a property className");
      throw new ClassLoadException(driverClassName, e);
    }
  }

}