package commands.pcommands;

import commands.*;
import utils.Exitable;

public final class Exit extends AbstractCommand {
  private final Exitable target;

  public Exit(Exitable target) {
    super(CommandInfo.of("exit", "Exit without saving collection", true, 0, false, ExecutionMode.CLIENT));
    this.target = target;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    target.exit();
    return ExecutionResult.valueOf(true, "Exit command executed successfully");
  }
}

