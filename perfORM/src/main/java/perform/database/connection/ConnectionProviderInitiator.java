package perform.database.connection;

import java.util.Properties;

/**
 * Manages creation and configuration of ConnectionProvider
 */
public class ConnectionProviderInitiator {
  public static final ConnectionProviderInitiator INSTANCE = new ConnectionProviderInitiator();

  private ConnectionProviderInitiator() {
  }

  public ConnectionProvider initiate(Properties configProps) {
    DriverManagerConnectionProvider connectionProvider = new DriverManagerConnectionProvider();
    connectionProvider.configure(configProps);
    return connectionProvider;
  }

}
