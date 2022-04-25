package server.commands.pcommands;

import commands.*;
import model.ModelDto;
import model.data.Model;
import server.collection.CollectionManager;
import server.collection.DtoToModelMapper;

import static commands.ExecutionMode.SERVER;

public final class Update<T extends Model> extends AbstractCommand {
  private final CollectionManager<T> collectionManager;

  public Update(CollectionManager<T> collectionManager) {
    super(CommandInfo.of("update", "Modify an existing element in collection", true, 1, CommandType.REQUIRES_DTO, SERVER));
    this.collectionManager = collectionManager;
  }


  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    String idStr = payload.getInlineArg();
    int id;
    try {
      id = Integer.parseInt(idStr);
    } catch (NumberFormatException e) {
      return ExecutionResult.valueOf(false, "Id should be an integer");
    }

    ModelDto dataValues = payload.getDataValues();
    T newModel = DtoToModelMapper.fromDto(dataValues);
    newModel.setId(id);
    if (!collectionManager.update(newModel)) {
      return ExecutionResult.valueOf(false, "Element with id " + id + " not updated");
    }

    return ExecutionResult.valueOf(true, "item updated");
  }
}