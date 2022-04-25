package exceptions;

public class UserAlreadyExistsException extends Exception {
  public UserAlreadyExistsException(String login) {
    super("User with login [" + login + "] already exists.");
  }
}
