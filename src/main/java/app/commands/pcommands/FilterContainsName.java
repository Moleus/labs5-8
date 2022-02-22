package app.commands.pcommands;

import app.collection.CollectionManager;
import app.collection.data.Flat;
import app.commands.AbstractCommand;
import app.commands.CommandInfo;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;

import java.util.Arrays;

import static app.commands.ExecutionMode.SERVER;

public final class FilterContainsName extends AbstractCommand {
  private final CollectionManager collectionManager;

  public FilterContainsName(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("filter_contains_name", "Print elements with specified string in names", true, 1, false, SERVER));
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