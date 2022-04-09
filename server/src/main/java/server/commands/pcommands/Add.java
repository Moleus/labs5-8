package server.commands.pcommands;

import commands.AbstractCommand;
import commands.CommandInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import model.ModelDto;
import model.data.Model;
import org.modelmapper.MappingException;
import server.collection.CollectionManager;
import server.collection.DtoToModelMapper;

import static commands.ExecutionMode.SERVER;

public final class Add<T extends Model> extends AbstractCommand {
  private final CollectionManager<T> collectionManager;

  public Add(CollectionManager<T> collectionManager) {
    super(CommandInfo.of("add", "Adds created object into the collection.", true, 0, true, SERVER));
    this.collectionManager = collectionManager;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    ModelDto dataValues = payload.getDataValues();

    try {
      T newModel = DtoToModelMapper.fromDto(dataValues);
      collectionManager.add(newModel);
    } catch (MappingException e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }

    return ExecutionResult.valueOf(true, "added new item in collection");
  }
}
