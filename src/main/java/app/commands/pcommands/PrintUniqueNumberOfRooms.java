package app.commands.pcommands;

import app.collection.CollectionManager;
import app.commands.AbstractCommand;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;
import app.commands.CommandInfo;

import java.util.Set;

import static app.commands.ExecutionMode.SERVER;

public final class PrintUniqueNumberOfRooms extends AbstractCommand {
  private final CollectionManager collectionManager;

  public PrintUniqueNumberOfRooms(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("print_unique_number_of_rooms", "Print set of 'numberOfRooms' values", true, 0, false, SERVER));
    this.collectionManager = collectionManager;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    Set<Long> numberOfRooms = collectionManager.getUniqueNumberOfRooms();
    String message = numberOfRooms.toString();
    return ExecutionResult.valueOf(true, message);
  }
}

