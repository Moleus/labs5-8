package perform.cfg;

import java.util.Properties;

public class ConfigurationHelper {
  public static Properties hideSensitive(Properties properties, String sesnitive) {
    Properties cloned = (Properties) properties.clone();
    cloned.computeIfPresent(sesnitive, (k, v) -> "***");
    return cloned;
  }
}
