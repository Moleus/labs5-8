package app.commands.pcommands;

import app.collection.CollectionManager;
import app.commands.CommandInfo;
import app.commands.AbstractCommand;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;

public final class Clear extends AbstractCommand {
  public Clear(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("clear", "Remove all elements from collection", true, 0, false));
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    // return ExecutionResult.valueOf(true, "Ok");
    // TODO
    return null;
  }
}

