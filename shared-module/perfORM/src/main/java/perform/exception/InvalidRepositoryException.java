package perform.exception;

public class InvalidRepositoryException extends PerformException {
  public InvalidRepositoryException(String message, Class<?> repoType) {
    super(message + " In repository [" + repoType.getSimpleName() + "]");
  }

  public InvalidRepositoryException(String message) {
    super(message);
  }
}
