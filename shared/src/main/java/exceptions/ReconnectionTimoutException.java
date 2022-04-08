package exceptions;

public class ReconnectionTimoutException extends Exception {
  public ReconnectionTimoutException() {
    super("Failed to reconnect");
  }
}
