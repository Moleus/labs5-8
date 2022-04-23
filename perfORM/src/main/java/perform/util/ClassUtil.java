package perform.util;

import perform.annotations.Collectible;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ClassUtil {
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
}