package app.commands.pcommands;

import app.collection.CollectionManager;
import app.collection.FlatBuilder;
import app.collection.data.Flat;
import app.commands.CommandInfo;
import app.commands.AbstractCommand;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;
import app.exceptions.ElementNotFoundException;
import app.exceptions.InvalidDataValues;

import static app.commands.ExecutionMode.SERVER;

public final class AddIfMin extends AbstractCommand {
  private final CollectionManager collectionManager;

  public AddIfMin(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("add_if_min", "Add new element if it's the least in collection", true, 0, true, SERVER));
    this.collectionManager = collectionManager;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    Object[] dataValues = payload.getDataValues();

    try {
      Flat newFlat = FlatBuilder.getInstance().buildAccessible(dataValues);
      Flat minFlat = collectionManager.getMin();
      if (newFlat.compareTo(minFlat) < 0) {
        collectionManager.add(newFlat);
        return ExecutionResult.valueOf(true, "added new element in collection");
      }
    } catch (InvalidDataValues | ElementNotFoundException e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }

    return ExecutionResult.valueOf(true, "element is not the least in collection. Skipped.");
  }
}

