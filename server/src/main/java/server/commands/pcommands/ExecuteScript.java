package server.commands.pcommands;

import commands.CommandInfo;
import commands.ExecutionMode;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import server.commands.AbstractCommand;

import exceptions.ScriptExecutionException;
import utils.Console;

public final class ExecuteScript extends AbstractCommand {
  public ExecuteScript() {
    super(CommandInfo.valueOf("execute_script", "Run all commands from specified file", true, 1, false, ExecutionMode.CLIENT));
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    String fileName = payload.getInlineArg();
    Object[] dataValues = payload.getDataValues();
    if (dataValues.length != 1) {
      return ExecutionResult.valueOf(false, "Invalid arguments number");
    }
    if (!(dataValues[0] instanceof Console)) {
      return ExecutionResult.valueOf(false, "Argument in array is not a console instance");
    }
    Console console = (Console) dataValues[0];
    try {
      console.executeScript(fileName);
    } catch (ScriptExecutionException e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }
    return ExecutionResult.valueOf(true, "Script execution started");
  }
}

