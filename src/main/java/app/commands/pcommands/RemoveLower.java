package app.commands.pcommands;

import app.collection.CollectionManager;
import app.commands.AbstractCommand;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;
import app.commands.CommandInfo;

public final class RemoveLower extends AbstractCommand {
  public RemoveLower(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("remove_lower", "Remove elements less than given", true, 0, true));
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    // return ExecutionResult.valueOf(true, "Ok");
    // TODO
    return null;
  }
}

