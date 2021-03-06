package commands.pcommands;

import commands.*;
import interfaces.Exitable;

public final class Exit extends AbstractCommand {
  private final Exitable target;

  public Exit(Exitable target) {
    super(CommandInfo.of("exit", "Exit without saving collection", true, 0, CommandType.OTHER, ExecutionMode.CLIENT));
    this.target = target;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    target.exit();
    return ExecutionResult.valueOf(true, "Exit command executed successfully");
  }
}

