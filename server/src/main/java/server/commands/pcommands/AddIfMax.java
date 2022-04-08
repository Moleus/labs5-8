package server.commands.pcommands;

import commands.AbstractCommand;
import commands.CommandInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import exceptions.ElementNotFoundException;
import exceptions.ValueConstraintsException;
import model.Model;
import model.ModelDto;
import model.builder.ModelBuilderWrapper;
import server.collection.CollectionManager;

import static commands.ExecutionMode.SERVER;

public final class AddIfMax extends AbstractCommand {
  private final CollectionManager collectionManager;

  public AddIfMax(CollectionManager collectionManager) {
    super(CommandInfo.of("add_if_max", "Add new element if it's the greatest in collection", true, 0, true, SERVER));
    this.collectionManager = collectionManager;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    ModelDto dataValues = payload.getDataValues();

    try {
      Model newModel = ModelBuilderWrapper.fromDto(dataValues);
      Model maxModel = collectionManager.getMax();
      if (newModel.compareTo(maxModel) > 0) {
        collectionManager.add(newModel);
        return ExecutionResult.valueOf(true, "added new element in collection");
      }
    } catch (ElementNotFoundException | ValueConstraintsException e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }

    return ExecutionResult.valueOf(true, "element is not the greatest in collection. Skipped.");
  }
}

