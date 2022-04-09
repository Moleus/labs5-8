package server.commands.pcommands;

import commands.AbstractCommand;
import commands.CommandInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import model.ModelDto;
import model.data.Model;
import server.collection.CollectionManager;
import server.collection.DtoToModelMapper;

import static commands.ExecutionMode.SERVER;

public final class Update<T extends Model> extends AbstractCommand {
  private final CollectionManager<T> collectionManager;

  public Update(CollectionManager<T> collectionManager) {
    super(CommandInfo.of("update", "Modify an existing element in collection", true, 1, true, SERVER));
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

    try {
      Model newModel = ModelBuilderWrapper.fromDto(dataValues);
      collectionManager.removeById(id);
      collectionManager.add(newModel);
    } catch (ValueConstraintsException e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    } catch (ElementNotFoundException e) {
      return ExecutionResult.valueOf(false, "Element with id " + id + " doesn't exist");
    }

    return ExecutionResult.valueOf(true, "item updated");
  }
}