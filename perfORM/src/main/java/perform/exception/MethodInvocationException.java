package perform.exception;

public class MethodInvocationException extends PerformException {
  public MethodInvocationException(String message) {
    super(message);
  }

  public MethodInvocationException(Class<?> clazz, String methodName, Throwable cause) {
    super("Failed to call method [" + methodName + "] on [" + clazz.getName() + "]", cause);
  }
}
