package commands;

/**
 * Immutable class which represents result of a command execution.
 * Use factory method for new instances creation.
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
