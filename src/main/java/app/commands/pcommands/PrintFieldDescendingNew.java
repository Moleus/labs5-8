package app.commands.pcommands;

import app.collection.CollectionManager;
import app.commands.AbstractCommand;
import app.commands.ExecutionPayload;
import app.commands.ExecutionResult;
import app.commands.CommandInfo;

import java.util.Arrays;

import static app.commands.ExecutionMode.SERVER;

public final class PrintFieldDescendingNew extends AbstractCommand {
  private final CollectionManager collectionManager;

  public PrintFieldDescendingNew(CollectionManager collectionManager) {
    super(CommandInfo.valueOf("print_field_descending_new", "Print 'new' values in descending order", true, 0, false, SERVER));
    this.collectionManager = collectionManager;
  }
  
  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    Boolean[] fieldsNew = collectionManager.getFieldDescendingNew();
    String message = Arrays.toString(fieldsNew);
    return ExecutionResult.valueOf(true, message);
  }
}
