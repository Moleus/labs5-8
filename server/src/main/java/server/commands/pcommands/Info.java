package server.commands.pcommands;

import commands.CommandInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import server.collection.CollectionManager;
import commands.AbstractCommand;

import static commands.ExecutionMode.SERVER;

public final class Info extends AbstractCommand {
  private final CollectionManager collectionManager;

  public Info(CollectionManager collectionManager) {
    super(CommandInfo.of("info", "Displays general information about collection", true, 0, false, SERVER));
    this.collectionManager = collectionManager;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    String collectionInformation = String.format("Collection of Flats: %nInit time: %s %nNumber of elements: %s%n", collectionManager.getCreationDateTime(), collectionManager.getSize());
    return ExecutionResult.valueOf(true, collectionInformation);
  }
}

