package server.commands.pcommands;

import commands.CommandInfo;
import commands.ExecutionMode;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import model.data.Flat;
import server.collection.CollectionManager;
import server.commands.AbstractCommand;

import java.util.Arrays;

public final class FilterContainsName extends AbstractCommand {
  private final CollectionManager collectionManager;

  public FilterContainsName(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("filter_contains_name", "Print elements with specified string in names", true, 1, false, ExecutionMode.SERVER));
    this.collectionManager = collectionManager;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    String filter = payload.getInlineArg();
    Flat[] elements = collectionManager.filterContainsName(filter);
    String message = Arrays.toString(elements);
    return ExecutionResult.valueOf(true, message);
  }
}