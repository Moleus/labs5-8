package exceptions;

public class ScriptExecutionException extends Exception {
  public ScriptExecutionException(String message, String scriptName) {
    super(String.format("Can't execute script '%s'. %s", scriptName, message));
  }
}
