package server.commands.pcommands;

import commands.*;
import exceptions.ElementNotFoundException;
import model.data.Model;
import server.collection.CollectionManager;
import user.User;

import static commands.ExecutionMode.SERVER;

public final class RemoveById<T extends Model> extends AbstractCommand {
  private final CollectionManager<T> collectionManager;

  public RemoveById(CollectionManager<T> collectionManager) {
    super(CommandInfo.of("remove_by_id", "remove an element with {id} from collection", true, 1, CommandType.OTHER, SERVER));
    this.collectionManager = collectionManager;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    String idStr = payload.getInlineArg();
    User user = payload.getUser();
    int id;
    try {
      id = Integer.parseInt(idStr);
    }
    catch (NumberFormatException e) {
      return ExecutionResult.valueOf(false, "Id should be an integer");
    }

    try {
      collectionManager.removeById(id, user);
    } catch (ElementNotFoundException e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }

    return ExecutionResult.valueOf(true, "item removed");
  }
}

