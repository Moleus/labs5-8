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

public final class AddIfMax extends AbstractCommand {
  private final CollectionManager collectionManager;

  public AddIfMax(CollectionManager collectionManager) {
    super(CommandInfo.of("add_if_max", "Add new element if it's the greatest in collection", true, 0, true, SERVER));
    this.collectionManager = collectionManager;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    Object[] dataValues = payload.getDataValues();

    try {
      Flat newFlat = FlatBuilder.getInstance().buildAccessible(dataValues);
      Flat maxFlat = collectionManager.getMax();
      if (newFlat.compareTo(maxFlat) > 0) {
        collectionManager.add(newFlat);
        return ExecutionResult.valueOf(true, "added new element in collection");
      }
    } catch (InvalidDataValues | ElementNotFoundException e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }

    return ExecutionResult.valueOf(true, "element is not the greatest in collection. Skipped.");
  }
}

