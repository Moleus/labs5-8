package commands.pcommands;

import collection.CollectionFilter;
import commands.AbstractCommand;
import commands.CommandInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;

import java.util.Set;

import static commands.ExecutionMode.CLIENT;

public final class PrintUniqueNumberOfRooms extends AbstractCommand {
  private final CollectionFilter collectionFilter;

  public PrintUniqueNumberOfRooms(CollectionFilter collectionManager) {
    super(CommandInfo.of("print_unique_number_of_rooms", "Print set of 'numberOfRooms' values", true, 0, false, CLIENT));
    this.collectionFilter = collectionManager;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    Set<Long> numberOfRooms = collectionFilter.getUniqueNumberOfRooms();
    String message = numberOfRooms.toString();
    return ExecutionResult.valueOf(true, message);
  }
}

