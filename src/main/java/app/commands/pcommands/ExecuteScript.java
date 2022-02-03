package app.commands.pcommands;

import app.commands.*;

public final class ExecuteScript extends AbstractCommand {
  public ExecuteScript(CommandManager commandManager) {
    super(CommandInfo.valueOf("execute_script", "Run all commands from specified file", true, 1, false));
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    // return ExecutionResult.valueOf(true, "Ok");
    // TODO
    return null;
  }
}

