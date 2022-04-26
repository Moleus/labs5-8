package server.commands.pcommands;

import commands.*;
import model.data.Model;
import server.collection.CollectionManager;

import static commands.ExecutionMode.SERVER;

public final class Clear<T extends Model> extends AbstractCommand {
  private final CollectionManager<T> collectionManager;

  public Clear(CollectionManager<T> collectionManager) {
    super(CommandInfo.of("clear", "Remove all elements from collection", true, 0, CommandType.OTHER, SERVER));
    this.collectionManager = collectionManager;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    collectionManager.clear(payload.getUser());
    return ExecutionResult.valueOf(true, "collection has been cleared");
  }
}

