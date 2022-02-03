package app.commands.pcommands;

import app.collection.CollectionManager;
import app.commands.CommandInfo;
import app.commands.AbstractCommand;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;

public final class AddIfMax extends AbstractCommand {
  public AddIfMax(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("add_if_max", "Add new element if it's the greatest in collection", true, 0, true));
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    // return ExecutionResult.valueOf(true, "Ok");
    // TODO
    return null;
  }
}

