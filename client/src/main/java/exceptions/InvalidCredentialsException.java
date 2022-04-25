package exceptions;

public class InvalidCredentialsException extends Exception {
  public InvalidCredentialsException() {
    super("Invalid login or password");
  }
}
