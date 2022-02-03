package app.common;

import app.commands.ExecutionPayload;

public class CommandRequest implements Request {
  private final String commandName;
  private final ExecutionPayload executionPayload;

  private CommandRequest(String commandName, ExecutionPayload executionPayload) {
    this.commandName = commandName;
    this.executionPayload = executionPayload;
  }

  public static CommandRequest valueOf(String commandName, ExecutionPayload executionPayload) {
    return new CommandRequest(commandName, executionPayload);
  }

  @Override
  public String getCommandName() {
    return commandName;
  }

  @Override
  public ExecutionPayload getExecutionPayload() {
    return executionPayload;
  }
}
