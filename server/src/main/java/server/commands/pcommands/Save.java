package server.commands.pcommands;

import commands.CommandInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import server.exceptions.StorageAccessException;
import server.collection.CollectionManager;
import server.commands.AbstractCommand;

import static commands.ExecutionMode.SERVER;

public final class Save extends AbstractCommand {
  private final CollectionManager collectionManager;

  public Save(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("save", "Save collection in a storage", true, 0, false, SERVER));
    this.collectionManager = collectionManager;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    try {
      collectionManager.saveCollection();
    } catch (StorageAccessException e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }

    return ExecutionResult.valueOf(true, "Ok");
  }
}

