package app.commands.pcommands;

import app.collection.CollectionManager;
import app.collection.FlatBuilder;
import app.collection.data.Flat;
import app.commands.AbstractCommand;
import app.commands.CommandInfo;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;
import app.exceptions.InvalidDataValues;

import static app.commands.ExecutionMode.SERVER;

public final class Add extends AbstractCommand {
  private final CollectionManager collectionManager;

  public Add(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("add", "Adds created object into the collection.", true, 0, true, SERVER));
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
