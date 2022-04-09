package server.commands.pcommands;

import commands.AbstractCommand;
import commands.CommandInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import exceptions.ElementNotFoundException;
import model.data.Model;
import server.collection.CollectionManager;

import static commands.ExecutionMode.SERVER;

public final class RemoveById<T extends Model> extends AbstractCommand {
  private final CollectionManager<T> collectionManager;

  public RemoveById(CollectionManager<T> collectionManager) {
    super(CommandInfo.of("remove_by_id", "remove an element with {id} from collection", true, 1, false, SERVER));
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

