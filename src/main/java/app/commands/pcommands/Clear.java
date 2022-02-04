package app.commands.pcommands;

import app.collection.CollectionManager;
import app.commands.CommandInfo;
import app.commands.AbstractCommand;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;

public final class Clear extends AbstractCommand {
  private final CollectionManager collectionManager;

  public Clear(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("clear", "Remove all elements from collection", true, 0, false));
    this.collectionManager = collectionManager;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    collectionManager.clear();
    return ExecutionResult.valueOf(true, "collection has been cleared");
  }
}

