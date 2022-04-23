package perform.cfg;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Log4j2
public final class Environment implements AvailableSettings {
  private static final Properties GLOBAL_PROPERTIES;

  static {
    GLOBAL_PROPERTIES = new Properties();

    try (InputStream stream = Environment.class.getResourceAsStream("perform.properties")) {
      GLOBAL_PROPERTIES.load(stream);
      log.debug("Properties loaded from 'perform.properties': {}", ConfigurationHelper.hideSensitive(GLOBAL_PROPERTIES, PASS));

    } catch (IOException e) {
      log.error("Failed to load properties from 'perform.properties'");
    }
  }

  public static Properties getProperties() {
    Properties copy = new Properties(GLOBAL_PROPERTIES);
    copy.putAll(GLOBAL_PROPERTIES);
    return copy;
  }
}
