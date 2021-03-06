package commands.pcommands;

import client.ScriptExecutor;
import commands.*;
import exceptions.ScriptExecutionException;

public final class ExecuteScript extends AbstractCommand {
  private final ScriptExecutor executor;

  public ExecuteScript(ScriptExecutor executor) {
    super(CommandInfo.of("execute_script", "Run all commands from specified file", true, 1, CommandType.OTHER, ExecutionMode.CLIENT));
    this.executor = executor;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    String fileName = payload.getInlineArg();
    try {
      executor.executeScript(fileName);
    } catch (ScriptExecutionException e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }
    return ExecutionResult.valueOf(true, "Script execution started");
  }
}

