package perform.util;

import java.util.HashMap;
import java.util.Map;

public class ClassUtil {
  private static final Map<Class<?>, Class<?>> boxedToPrimitive = new HashMap<>();

  static {
    boxedToPrimitive.put(Boolean.class, boolean.class);
    boxedToPrimitive.put(Byte.class, byte.class);
    boxedToPrimitive.put(Character.class, char.class);
    boxedToPrimitive.put(Double.class, double.class);
    boxedToPrimitive.put(Float.class, float.class);
    boxedToPrimitive.put(Integer.class, int.class);
    boxedToPrimitive.put(Long.class, long.class);
    boxedToPrimitive.put(Short.class, short.class);
  }

  public static Class<?> resolveToPrimitive(Class<?> boxedType) {
    return boxedType.isPrimitive() ? boxedType : boxedToPrimitive.get(boxedType);
  }
}