package server.commands.pcommands;
import commands.CommandInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import server.collection.CollectionManager;
import commands.AbstractCommand;

import server.model.FlatBuilder;
import model.data.Flat;
import server.exceptions.InvalidDataValues;

import static commands.ExecutionMode.SERVER;

public final class Add extends AbstractCommand {
  private final CollectionManager collectionManager;

  public Add(CollectionManager collectionManager) {
    super(CommandInfo.of("add", "Adds created object into the collection.", true, 0, true, SERVER));
    this.collectionManager = collectionManager;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    Object[] dataValues = payload.getDataValues();

    try {
      Flat newFlat = FlatBuilder.getInstance().buildAccessible(dataValues);
      collectionManager.add(newFlat);
    } catch (InvalidDataValues e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }

    return ExecutionResult.valueOf(true, "added new item in collection");
  }
}
