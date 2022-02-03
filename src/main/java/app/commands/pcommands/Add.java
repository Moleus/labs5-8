package app.commands.pcommands;

import app.collection.CollectionManager;
import app.collection.data.Flat;
import app.commands.AbstractCommand;
import app.commands.CommandInfo;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;

public final class Add extends AbstractCommand {
  private final CollectionManager collectionManager;
  // private final productBuilder productBuilder;
  
  public Add(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("add", "Adds created object into the collection.", true, 0, true));

    this.collectionManager = collectionManager;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    Object dataObject = payload.getDataObject();
    if (!(dataObject instanceof Flat)) {
      return ExecutionResult.valueOf(false, "object is null");
    }

    Flat newFlat = (Flat) dataObject;
    collectionManager.add(newFlat);

    return ExecutionResult.valueOf(true, "added new item in collection");
  }
}
