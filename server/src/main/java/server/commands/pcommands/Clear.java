package server.commands.pcommands;

import commands.CommandInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import server.collection.CollectionManager;
import server.commands.AbstractCommand;

import static commands.ExecutionMode.SERVER;

public final class Clear extends AbstractCommand {
  private final CollectionManager collectionManager;

  public Clear(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("clear", "Remove all elements from collection", true, 0, false, SERVER));
    this.collectionManager = collectionManager;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    collectionManager.clear();
    return ExecutionResult.valueOf(true, "collection has been cleared");
  }
}

