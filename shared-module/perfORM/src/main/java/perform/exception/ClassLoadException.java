package perform.exception;

public class ClassLoadException extends PerformException {
  public ClassLoadException(Object loadedClass, Throwable cause) {
    super("Failed to load Class [" + loadedClass + "]. Cause: ", cause);
  }
}
