package perform.exception;

public class PerformException extends RuntimeException {
  public PerformException(String message) {
    super(message);
  }

  public PerformException(String message, Throwable cause) {
    super(message, cause);
  }
}
