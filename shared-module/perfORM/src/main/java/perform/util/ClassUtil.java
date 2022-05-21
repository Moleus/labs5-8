package perform.util;

import java.util.HashMap;
import java.util.Map;

public class ClassUtil {
  private static final Map<Class<?>, Class<?>> primitiveToBoxed = new HashMap<>();

  static {
    primitiveToBoxed.put(boolean.class, Boolean.class);
    primitiveToBoxed.put(byte.class, Byte.class);
    primitiveToBoxed.put(char.class, Character.class);
    primitiveToBoxed.put(double.class, Double.class);
    primitiveToBoxed.put(float.class, Float.class);
    primitiveToBoxed.put(int.class, Integer.class);
    primitiveToBoxed.put(long.class, Long.class);
    primitiveToBoxed.put(short.class, Short.class);
    primitiveToBoxed.put(byte[].class, byte[].class);
  }

  public static Class<?> resolveToBoxed(Class<?> boxedType) {
    return boxedType.isPrimitive() ? primitiveToBoxed.get(boxedType) : boxedType;
  }
}