package server.commands.pcommands;

import commands.CommandInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import server.collection.CollectionManager;
import commands.AbstractCommand;

import java.util.Set;

import static commands.ExecutionMode.SERVER;

public final class PrintUniqueNumberOfRooms extends AbstractCommand {
  private final CollectionManager collectionManager;

  public PrintUniqueNumberOfRooms(CollectionManager collectionManager) {
    super(CommandInfo.of("print_unique_number_of_rooms", "Print set of 'numberOfRooms' values", true, 0, false, SERVER));
    this.collectionManager = collectionManager;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    Set<Long> numberOfRooms = collectionManager.getUniqueNumberOfRooms();
    String message = numberOfRooms.toString();
    return ExecutionResult.valueOf(true, message);
  }
}

