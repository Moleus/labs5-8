package exceptions;

public class ConnectionIsDownException extends Exception {
  public ConnectionIsDownException(String message) {
    super(message);
  }
}
