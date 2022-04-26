package perform.util;

import perform.exception.PerformException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

public enum JdbcColumnTypes {
  INSTANCE {
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Class<?> resolvePrimitiveType(Class<?> type) {
      return javaToDbType.entrySet().stream()
          .filter(e -> e.getKey().isAssignableFrom(type))
          .map(e -> (Class<?>) e.getValue())
          .findFirst()
          .orElseGet(() -> (Class) ClassUtil.resolveToPrimitive(type));
    }
  };

  private static final Map<Class<?>, Class<?>> javaToDbType = new LinkedHashMap<>();

  static {
    javaToDbType.put(Enum.class, String.class);
    javaToDbType.put(LocalDateTime.class, LocalDateTime.class);
  }

  public abstract Class<?> resolvePrimitiveType(Class<?> type);

  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <T> T castUncommonType(Object value, Class<T> type) {
    if (Enum.class.isAssignableFrom(type)) {
      // TODO: catch ClassCastException if String in table is malformed.
      try {
        return (T) Enum.valueOf((Class<Enum>) type, (String) value);
      } catch (IllegalArgumentException e) {
        throw new PerformException("Invalid Enum value in table: " + value);
      }
    }
    return (T) value;
  }
}