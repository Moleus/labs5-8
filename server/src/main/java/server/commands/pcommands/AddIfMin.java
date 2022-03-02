package server.commands.pcommands;

import commands.AbstractCommand;
import commands.CommandInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import exceptions.ElementNotFoundException;
import model.data.Flat;
import server.collection.CollectionManager;
import server.exceptions.InvalidDataValues;
import server.model.FlatBuilder;

import static commands.ExecutionMode.SERVER;

public final class AddIfMin extends AbstractCommand {
  private final CollectionManager collectionManager;

  public AddIfMin(CollectionManager collectionManager) {
    super(CommandInfo.of("add_if_min", "Add new element if it's the least in collection", true, 0, true, SERVER));
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

