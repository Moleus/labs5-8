package server.commands.pcommands;

import commands.AbstractCommand;
import commands.CommandInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import exceptions.ElementNotFoundException;
import model.ModelDto;
import model.data.Model;
import org.modelmapper.MappingException;
import server.collection.CollectionManager;
import server.collection.DtoToModelMapper;

import static commands.ExecutionMode.SERVER;

public final class AddIfMin<T extends Model> extends AbstractCommand {
  private final CollectionManager<T> collectionManager;

  public AddIfMin(CollectionManager<T> collectionManager) {
    super(CommandInfo.of("add_if_min", "Add new element if it's the least in collection", true, 0, true, SERVER));
    this.collectionManager = collectionManager;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    ModelDto dataValues = payload.getDataValues();

    try {
      T newModel = DtoToModelMapper.fromDto(dataValues);
      T minModel = collectionManager.getMin();
      if (newModel.compareTo(minModel) < 0) {
        collectionManager.add(newModel);
        return ExecutionResult.valueOf(true, "added new element in collection");
      }
    } catch (ElementNotFoundException | MappingException e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }

    return ExecutionResult.valueOf(true, "element is not the least in collection. Skipped.");
  }
}

