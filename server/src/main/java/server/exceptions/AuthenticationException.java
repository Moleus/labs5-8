package server.exceptions;

public class AuthenticationException extends RuntimeException {
  public AuthenticationException(String login) {
    super("Authentication failed for user: " + login);
  }

  public AuthenticationException() {
    super("Authentication failed: Unknown user");
  }
}
