package communication;

import commands.ExecutionResult;

public class CommandResponse implements Response {
  private final ExecutionResult result;
  private CommandResponse(ExecutionResult result) {
    this.result = result;
  }

  public static CommandResponse valueOf(ExecutionResult result) {
    return new CommandResponse(result);
  }

  @Override
  public ExecutionResult getExecutionResult() {
    return result;
  }
}
