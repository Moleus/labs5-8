package server.commands.pcommands;

import commands.*;
import model.ModelDto;
import model.data.Model;
import org.modelmapper.MappingException;
import server.collection.CollectionManager;
import server.collection.DtoToModelMapper;
import user.User;

import static commands.ExecutionMode.SERVER;

public final class Add<T extends Model> extends AbstractCommand {
  private final CollectionManager<T> collectionManager;

  public Add(CollectionManager<T> collectionManager) {
    super(CommandInfo.of("add", "Adds created object into the collection.", true, 0, CommandType.REQUIRES_DTO, SERVER));
    this.collectionManager = collectionManager;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    ModelDto modelDto = payload.getData();
    User user = payload.getUser();

    try {
      T newModel = DtoToModelMapper.fromDto(modelDto);
      collectionManager.add(newModel, user);
    } catch (MappingException e) {
      return ExecutionResult.valueOf(false, e.getMessage());
    }

    return ExecutionResult.valueOf(true, "added new item in collection");
  }
}
