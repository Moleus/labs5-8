package perform.util;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StringUtil {
  public static String addBrackets(Object target) {
    if (target == null || target.toString().isEmpty()) return "";
    return "(" + target + ")";
  }

  public static String join(String delim, Collection<?> collection) {
    return collection.stream().map(Object::toString).collect(Collectors.joining(delim));
  }

  public static String join(String delim, Object... elements) {
    return Stream.of(elements).map(Object::toString)
        .filter(s -> s != null && !s.isEmpty())
        .collect(Collectors.joining(delim));
  }
}
