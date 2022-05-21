package perform.exception;

public class DuplicateKeyException extends PerformException {
  public DuplicateKeyException(String message) {
    super(message);
  }

  public DuplicateKeyException(Class<?> clazz, String keyName) {
    super("Found duplicated [" + keyName + "] in " + clazz.getName());
  }
}