package app.commands.pcommands;

import app.commands.CommandInfo;
import app.commands.AbstractCommand;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;

public final class Exit extends AbstractCommand {
  public Exit() {
    super(CommandInfo.valueOf("exit", "Exit without saving collection", true, 0, false));
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    // FIXME: This command does nothing, because script executed in console.
    throw new UnsupportedOperationException("This method shouldn't be executed for command exit");
  }
}

