package app.commands.pcommands;

import app.collection.CollectionManager;
import app.commands.CommandInfo;
import app.commands.AbstractCommand;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;

import static app.commands.ExecutionMode.SERVER;

public final class Info extends AbstractCommand {
  private final CollectionManager collectionManager;

  public Info(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("info", "Displays general information about collection", true, 0, false, SERVER));
    this.collectionManager = collectionManager;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    String collectionInformation = String.format("Collection of Flats: %nInit time: %s %nNumber of elements: %s%n", collectionManager.getCreationDateTime(), collectionManager.getSize());
    return ExecutionResult.valueOf(true, collectionInformation);
  }
}

