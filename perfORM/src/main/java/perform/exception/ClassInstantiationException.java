package perform.exception;

import java.lang.reflect.Constructor;

public class ClassInstantiationException extends PerformException {
  public ClassInstantiationException(Class<?> clazz, String message, Throwable cause) {
    super("Failed to instantiate [" + clazz.getName() + "]: " + message, cause);
  }

  public ClassInstantiationException(Constructor<?> clazz, String message, Throwable cause) {
    this(clazz.getDeclaringClass(), message, cause);
  }
}
