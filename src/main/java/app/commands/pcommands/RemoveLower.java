package app.commands.pcommands;

import app.collection.CollectionManager;
import app.collection.FlatBuilder;
import app.collection.data.Flat;
import app.commands.AbstractCommand;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;
import app.commands.CommandInfo;
import app.exceptions.InvalidDataValues;

import static app.commands.ExecutionMode.SERVER;

public final class RemoveLower extends AbstractCommand {
  private final CollectionManager collectionManager;

  public RemoveLower(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("remove_lower", "Remove elements less than given", true, 0, true, SERVER));
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

