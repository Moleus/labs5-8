package app.commands.pcommands;

import app.commands.CommandInfo;
import app.commands.AbstractCommand;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;
import app.utils.Console;

import static app.commands.ExecutionMode.CLIENT;

public final class Exit extends AbstractCommand {
  public Exit() {
    super(CommandInfo.valueOf("exit", "Exit without saving collection", true, 0, false, CLIENT));
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    Object[] dataValues = payload.getDataValues();
    if (dataValues.length != 1) {
      return ExecutionResult.valueOf(false, "Invalid arguments number");
    }
    if (!(dataValues[0] instanceof Console)) {
      return ExecutionResult.valueOf(false, "Argument in array is not a Console instance");
    }
    Console console = (Console) dataValues[0];
    console.exit();
    return ExecutionResult.valueOf(true, "Exit command executed successfully");
  }
}

