package app.commands.pcommands;

import app.collection.CollectionManager;
import app.commands.AbstractCommand;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;
import app.commands.CommandInfo;

public final class RemoveById extends AbstractCommand {
  public RemoveById(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("remove_by_id", "remove an element with {id} from collection", true, 1, false));
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    // return ExecutionResult.valueOf(true, "Ok");
    // TODO
    return null;
  }
}

