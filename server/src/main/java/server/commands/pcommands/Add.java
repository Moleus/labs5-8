package server.commands.pcommands;

import commands.*;
import model.ModelDto;
import model.data.Model;
import org.modelmapper.MappingException;
import server.collection.CollectionManager;
import server.collection.DtoToModelMapper;

import static commands.ExecutionMode.SERVER;

public final class Add<T extends Model> extends AbstractCommand {
  private final CollectionManager<T> collectionManager;

  public Add(CollectionManager<T> collectionManager) {
    super(CommandInfo.of("add", "Adds created object into the collection.", true, 0, CommandType.REQUIRES_DTO, SERVER));
    this.collectionManager = collectionManager;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    Object data = payload.getData();
    if (!(data instanceof ModelDto modelDto)) {
      return ExecutionResult.valueOf(false, "Assumed to get a ModelDto from payload");
    }

    try {
      T newModel = DtoToModelMapper.fromDto(modelDto);
      collectionManager.add(newModel);
    } catch (MappingException e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }

    return ExecutionResult.valueOf(true, "added new item in collection");
  }
}
