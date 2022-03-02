package commands.pcommands;

import commands.AbstractCommand;
import commands.CommandInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import model.CollectionFilter;

import static commands.ExecutionMode.CLIENT;

public final class Info extends AbstractCommand {
  private final CollectionFilter collectionFilter;

  public Info(CollectionFilter collectionManager) {
    super(CommandInfo.of("info", "Displays general information about collection", true, 0, false, CLIENT));
    this.collectionFilter = collectionManager;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    String collectionInformation = String.format("Collection of Flats: %nInit time: %s %nNumber of elements: %s%n", collectionFilter.getCreationDateTime(), collectionFilter.getSize());
    return ExecutionResult.valueOf(true, collectionInformation);
  }
}

