package commands.pcommands;

import commands.AbstractCommand;
import commands.CommandInfo;
import commands.ExecutionPayload;
import commands.ExecutionResult;
import model.CollectionFilter;

import java.util.Arrays;

import static commands.ExecutionMode.CLIENT;

public final class PrintFieldDescendingNew extends AbstractCommand {
  private final CollectionFilter collectionManager;

  public PrintFieldDescendingNew(CollectionFilter collectionFilter) {
    super(CommandInfo.of("print_field_descending_new", "Print 'new' values in descending order", true, 0, false, CLIENT));
    this.collectionManager = collectionFilter;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    Boolean[] fieldsNew = collectionManager.getFieldDescendingNew();
    String message = Arrays.toString(fieldsNew);
    return ExecutionResult.valueOf(true, message);
  }
}
