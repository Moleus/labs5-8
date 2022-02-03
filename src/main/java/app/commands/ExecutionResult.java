package app.commands;

/**
 * Package private immutable class which represents result of a command execution.
 * Factory method is used to create new instances.
 */
public class ExecutionResult {
  private final boolean success;
  private final String message;

  private ExecutionResult(boolean success, String message) {
    this.success = success;
    this.message = message;
  }

  public static ExecutionResult valueOf(boolean success, String message) {
    return new ExecutionResult(success, message);
  }

  public boolean isSuccess() {
    return success;
  }

  public String getMessage() {
    return message;
  }

}
