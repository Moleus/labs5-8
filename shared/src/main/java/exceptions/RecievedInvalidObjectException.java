package exceptions;

public class RecievedInvalidObjectException extends RuntimeException {
  public RecievedInvalidObjectException(Class<?> expected, Object actual) {
    super(String.format("Recieved object of Type '%s', expected a '%s'", actual, expected));
  }
}
