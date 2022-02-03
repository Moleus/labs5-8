package app.commands.pcommands;

import app.collection.CollectionManager;
import app.commands.CommandInfo;
import app.commands.AbstractCommand;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;
import app.storage.Storage;

public final class Save extends AbstractCommand {
  public Save(CollectionManager collectionManager, Storage storageManager) {
    super(CommandInfo.valueOf("save", "Save collection in a storage", true, 0, false));
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    // return ExecutionResult.valueOf(true, "Ok");
    // TODO
    return null;
  }
}

