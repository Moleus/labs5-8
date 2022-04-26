package server.commands.pcommands;

import commands.*;
import model.ModelDto;
import model.data.Model;
import server.collection.CollectionManager;
import server.collection.DtoToModelMapper;

public final class RemoveLower<T extends Model> extends AbstractCommand {
  private final CollectionManager<T> collectionManager;

  public RemoveLower(CollectionManager<T> collectionManager) {
    super(CommandInfo.of("remove_lower", "Remove elements less than given", true, 0, CommandType.REQUIRES_DTO, ExecutionMode.SERVER));
    this.collectionManager = collectionManager;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    Object data = payload.getData();
    if (!(data instanceof ModelDto modelDto)) {
      return ExecutionResult.valueOf(false, "Assumed to get a ModelDto from payload");
    }

    T upperBoundFlat = DtoToModelMapper.fromDto(modelDto);
    if (collectionManager.removeLower(upperBoundFlat)) {
      return ExecutionResult.valueOf(true, "Lower elements successfully removed");
    }
    return ExecutionResult.valueOf(true, "No elements removed");
  }
}

