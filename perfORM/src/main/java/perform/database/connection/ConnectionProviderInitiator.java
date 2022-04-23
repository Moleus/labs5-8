package perform.database.connection;

import perform.cfg.AvailableSettings;

import java.util.Map;
import java.util.Properties;

/**
 * Manages creation and configuration of ConnectionProvider
 */
public class ConnectionProviderInitiator {
  public static ConnectionProviderInitiator INSTANCE = new ConnectionProviderInitiator();

  private ConnectionProviderInitiator() {
  }

  public ConnectionProvider initiate(Properties configProps) {
    Properties connectionProperties = getConnectionProperties(configProps);
    DriverManagerConnectionProvider connectionProvider = new DriverManagerConnectionProvider();
    connectionProvider.configure(connectionProperties);
    return connectionProvider;
  }

  /**
   * Return only properties which are needed for JDBC connection.
   * They start with 'perform.connection'.
   *
   * @param properties global configuration properties to choose from
   */
  private static Properties getConnectionProperties(Properties properties) {
    Properties connectionProps = new Properties();
    for (Map.Entry<Object, Object> entry : properties.entrySet()) {
      if (!(entry.getKey() instanceof String key) || !(entry.getValue() instanceof String value)) {
        continue;
      }
      if (!key.startsWith(AvailableSettings.CONNECTION_PREFFIX)) {
        continue;
      }
      connectionProps.setProperty(
          key.substring(AvailableSettings.CONNECTION_PREFFIX.length() + 1),
          value
      );
    }
    return connectionProps;
  }
}
