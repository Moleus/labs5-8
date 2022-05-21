package exceptions;

public class ReceivedInvalidObjectException extends RuntimeException {
  public ReceivedInvalidObjectException(Class<?> expected, Object actual) {
    super(String.format("Received object of Type '%s', expected a '%s'", actual, expected));
  }

  public ReceivedInvalidObjectException() {
    super("Received an object of unknown type");
  }
}
