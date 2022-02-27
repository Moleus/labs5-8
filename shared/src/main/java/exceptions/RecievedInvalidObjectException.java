package exceptions;

import java.io.InvalidObjectException;

public class RecievedInvalidObjectException extends InvalidObjectException {
  public RecievedInvalidObjectException(String message) {
    super(message);
  }
  public RecievedInvalidObjectException() {
      super("Recieved invliad object");
  }
}
