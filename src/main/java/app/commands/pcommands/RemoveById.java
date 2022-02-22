package app.commands.pcommands;

import app.collection.CollectionManager;
import app.commands.AbstractCommand;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;
import app.commands.CommandInfo;
import app.exceptions.ElementNotFoundException;

import static app.commands.ExecutionMode.SERVER;

public final class RemoveById extends AbstractCommand {
  private final CollectionManager collectionManager;

  public RemoveById(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("remove_by_id", "remove an element with {id} from collection", true, 1, false, SERVER));
    this.collectionManager = collectionManager;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    String idStr = payload.getInlineArg();
    int id;
    try {
      id = Integer.parseInt(idStr);
    }
    catch (NumberFormatException e) {
      return ExecutionResult.valueOf(false, "Id should be an integer");
    }

    try {
      collectionManager.removeById(id);
    } catch (ElementNotFoundException e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }

    return ExecutionResult.valueOf(true, "item removed");
  }
}

