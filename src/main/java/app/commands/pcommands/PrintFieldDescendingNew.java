package app.commands.pcommands;

import app.collection.CollectionManager;
import app.commands.AbstractCommand;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;
import app.commands.CommandInfo;

public final class PrintFieldDescendingNew extends AbstractCommand {
  public PrintFieldDescendingNew(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("print_field_descending_new", "Print 'new' values in descending order", true, 0, false));
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    // return ExecutionResult.valueOf(true, "Ok");
    // TODO
    return null;
  }
}
