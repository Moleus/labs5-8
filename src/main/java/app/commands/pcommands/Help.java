package app.commands.pcommands;

import app.commands.*;

public final class Help extends AbstractCommand {
  public Help(CommandManager commandManager) {
    super(CommandInfo.valueOf("help", "Shows info about accessible commands", true, 0, false));
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    // return ExecutionResult.valueOf(true, "Ok");
    // TODO
    return null;
  }
}

