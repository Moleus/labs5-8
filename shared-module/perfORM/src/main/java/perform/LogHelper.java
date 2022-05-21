package perform;

public class LogHelper {
  public static String jdbcUrlNotSpecified(String propertyKey) {
    return "JDBC connection url was not specified in " + propertyKey;
  }
}
