package app.commands.pcommands;

import app.collection.CollectionManager;
import app.commands.AbstractCommand;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;
import app.commands.CommandInfo;

public final class PrintUniqueNumberOfRooms extends AbstractCommand {
  public PrintUniqueNumberOfRooms(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("print_unique_number_of_rooms", "Print set of 'numberOfRooms' values", true, 0, false));
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    // return ExecutionResult.valueOf(true, "Ok");
    // TODO
    return null;
  }
}

