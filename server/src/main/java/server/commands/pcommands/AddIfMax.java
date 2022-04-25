package server.commands.pcommands;

import commands.*;
import exceptions.ElementNotFoundException;
import model.ModelDto;
import model.data.Model;
import org.modelmapper.MappingException;
import server.collection.CollectionManager;
import server.collection.DtoToModelMapper;

import static commands.ExecutionMode.SERVER;

public final class AddIfMax<T extends Model> extends AbstractCommand {
  private final CollectionManager<T> collectionManager;

  public AddIfMax(CollectionManager<T> collectionManager) {
    super(CommandInfo.of("add_if_max", "Add new element if it's the greatest in collection", true, 0, CommandType.REQUIRES_DTO, SERVER));
    this.collectionManager = collectionManager;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    ModelDto dataValues = payload.getDataValues();

    try {
      T newModel = DtoToModelMapper.fromDto(dataValues);
      T maxModel = collectionManager.getMax();
      if (newModel.compareTo(maxModel) > 0) {
        collectionManager.add(newModel);
        return ExecutionResult.valueOf(true, "added new element in collection");
      }
    } catch (ElementNotFoundException | MappingException e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }

    return ExecutionResult.valueOf(true, "element is not the greatest in collection. Skipped.");
  }
}

