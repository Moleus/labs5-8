package perform.exception;

public class FieldNotFoundException extends InvalidRepositoryException {
  public FieldNotFoundException(String fieldName, Class<?> clazz) {
    super("Field [" + fieldName + "] not found in " + clazz.getName());
  }
}
