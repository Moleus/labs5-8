package exceptions;

public class ReconnectionTimeoutException extends RuntimeException {
  public ReconnectionTimeoutException() {
    super("Can't reconnect. Timeout exceeded");
  }
}
