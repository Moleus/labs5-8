package server.commands.pcommands;

import commands.AbstractCommand;
import commands.CommandInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import exceptions.ValueConstraintsException;
import model.Model;
import model.ModelDto;
import model.builder.ModelBuilderWrapper;
import server.collection.CollectionManager;

import static commands.ExecutionMode.SERVER;

public final class Add extends AbstractCommand {
  private final CollectionManager collectionManager;

  public Add(CollectionManager collectionManager) {
    super(CommandInfo.of("add", "Adds created object into the collection.", true, 0, true, SERVER));
    this.collectionManager = collectionManager;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    ModelDto dataValues = payload.getDataValues();

    try {
      Model newFlat = ModelBuilderWrapper.fromDto(dataValues);
      collectionManager.add(newFlat);
    } catch (ValueConstraintsException e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }

    return ExecutionResult.valueOf(true, "added new item in collection");
  }
}
