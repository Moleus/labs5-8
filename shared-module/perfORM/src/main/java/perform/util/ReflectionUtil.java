package perform.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

public class ReflectionUtil {
  public static void makeAccessible(Constructor<?> constructor) {
    if (!Modifier.isPublic(constructor.getModifiers()) || !Modifier.isPublic(constructor.getDeclaringClass().getModifiers())) {
      constructor.setAccessible(true);
    }
  }
}
