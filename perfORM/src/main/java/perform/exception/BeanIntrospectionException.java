package perform.exception;

public class BeanIntrospectionException extends PerformException {
  public BeanIntrospectionException(Class<?> clazz, String message, Throwable cause) {
    super("Failed to introspect bean [" + clazz.getName() + "]: " + message, cause);
  }
}
