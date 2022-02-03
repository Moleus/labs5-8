package app.commands.pcommands;

import app.collection.CollectionManager;
import app.commands.CommandInfo;
import app.commands.AbstractCommand;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;

public final class AddIfMin extends AbstractCommand {
  public AddIfMin(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("add_if_min", "Add new element if it's the least in collection", true, 0, true)); 
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    // return ExecutionResult.valueOf(true, "Ok");
    // TODO
    return null;
  }
}

