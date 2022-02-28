package server.commands.pcommands;

import commands.CommandInfo;
import commands.ExecutionMode;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import server.exceptions.InvalidDataValues;
import model.data.Flat;
import server.collection.CollectionManager;
import commands.AbstractCommand;
import server.model.FlatBuilder;

public final class RemoveLower extends AbstractCommand {
  private final CollectionManager collectionManager;

  public RemoveLower(CollectionManager collectionManager) {
    super(CommandInfo.of("remove_lower", "Remove elements less than given", true, 0, true, ExecutionMode.SERVER));
    this.collectionManager = collectionManager;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    Object[] dataValues = payload.getDataValues();

    Flat upperBoundFlat;
    try {
      upperBoundFlat = FlatBuilder.getInstance().buildAccessible(dataValues);
    } catch (InvalidDataValues e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }
    if (collectionManager.removeLower(upperBoundFlat)) {
      return ExecutionResult.valueOf(true, "Lower elements successfully removed");
    }
    return ExecutionResult.valueOf(true, "No elements removed");
  }
}

