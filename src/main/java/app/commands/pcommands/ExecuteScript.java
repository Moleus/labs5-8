package app.commands.pcommands;

import app.commands.*;

public final class ExecuteScript extends AbstractCommand {
  public ExecuteScript(CommandManager commandManager) {
    super(CommandInfo.valueOf("execute_script", "Run all commands from specified file", true, 1, false));
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    // FIXME: This command does nothing, because script executed in console.
    throw new UnsupportedOperationException("This method shouldn't be executed for command execute_script");
  }
}

