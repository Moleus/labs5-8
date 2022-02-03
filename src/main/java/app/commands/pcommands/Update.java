package app.commands.pcommands;

import app.collection.CollectionManager;
import app.commands.*;

public final class Update extends AbstractCommand {
  public Update(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("update", "", true, 1, true));
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    // return ExecutionResult.valueOf(true, "Ok");
    // TODO
    return null;
  }
}

