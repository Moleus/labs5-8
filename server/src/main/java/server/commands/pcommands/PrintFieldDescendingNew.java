package server.commands.pcommands;

import commands.CommandInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import server.collection.CollectionManager;
import server.commands.AbstractCommand;

import java.util.Arrays;

import static commands.ExecutionMode.SERVER;

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
