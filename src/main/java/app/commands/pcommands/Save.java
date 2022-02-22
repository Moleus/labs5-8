package app.commands.pcommands;

import app.collection.CollectionManager;
import app.commands.CommandInfo;
import app.commands.AbstractCommand;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;
import app.exceptions.StorageAccessException;

import static app.commands.ExecutionMode.SERVER;

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

