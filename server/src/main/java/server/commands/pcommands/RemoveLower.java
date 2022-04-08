package server.commands.pcommands;

import commands.*;
import exceptions.ValueConstraintsException;
import model.Model;
import model.ModelDto;
import model.builder.ModelBuilderWrapper;
import server.collection.CollectionManager;

public final class RemoveLower extends AbstractCommand {
  private final CollectionManager collectionManager;

  public RemoveLower(CollectionManager collectionManager) {
    super(CommandInfo.of("remove_lower", "Remove elements less than given", true, 0, true, ExecutionMode.SERVER));
    this.collectionManager = collectionManager;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    ModelDto dataValues = payload.getDataValues();

    Model upperBoundFlat;
    try {
      upperBoundFlat = ModelBuilderWrapper.fromDto(dataValues);
    } catch (ValueConstraintsException e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }
    if (collectionManager.removeLower(upperBoundFlat)) {
      return ExecutionResult.valueOf(true, "Lower elements successfully removed");
    }
    return ExecutionResult.valueOf(true, "No elements removed");
  }
}

