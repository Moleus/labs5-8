package perform.util;

import perform.annotations.Collectible;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

  public static List<Field> getRecursiveFields(Class<?> clazz) {
    List<Field> recursiveFields = new ArrayList<>();
    for (Field field : clazz.getDeclaredFields()) {
      Class<?> fieldType = field.getType();
      if (fieldType.getAnnotation(Collectible.class) != null) {
        recursiveFields.addAll(getRecursiveFields(fieldType));
      }
      recursiveFields.add(field);
    }
    return recursiveFields;
  }

  public static Class<?> resolveToPrimitive(Class<?> boxedType) {
    return boxedType.isPrimitive() ? boxedType : boxedToPrimitive.get(boxedType);
  }
}