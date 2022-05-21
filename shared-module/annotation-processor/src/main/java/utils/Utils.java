package utils;

public class Utils {
  public static String capitalize(String str) {
    if (str == null || str.length() == 0) {
      return str;
    }
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }

  public static String decapitalize(String str) {
    if (str == null || str.length() == 0) {
      return str;
    }
    return str.substring(0, 1).toLowerCase() + str.substring(1);
  }
}
