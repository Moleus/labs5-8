package app.commands.pcommands;

import app.collection.CollectionManager;
import app.commands.AbstractCommand;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;
import app.commands.CommandInfo;

public final class FilterContainsName extends AbstractCommand {
  public FilterContainsName(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("filter_contains_name", "Print elements with specified string in names", true, 1, false));
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    // return ExecutionResult.valueOf(true, "Ok");
    // TODO
    return null;
  }
}

