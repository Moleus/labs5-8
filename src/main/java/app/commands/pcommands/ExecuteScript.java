package app.commands.pcommands;

import app.commands.*;
import app.exceptions.ScriptExecutionException;
import app.utils.Console;

import static app.commands.ExecutionMode.CLIENT;

public final class ExecuteScript extends AbstractCommand {
  public ExecuteScript() {
    super(CommandInfo.valueOf("execute_script", "Run all commands from specified file", true, 1, false, CLIENT));
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

