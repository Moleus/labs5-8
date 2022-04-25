package commands.pcommands;

import collection.CollectionFilter;
import commands.*;
import model.data.Model;

import java.util.Arrays;

public final class FilterContainsName extends AbstractCommand {
  private final CollectionFilter collectionFilter;

  public FilterContainsName(CollectionFilter collectionFilter) {
    super(CommandInfo.of("filter_contains_name", "Print elements with specified string in names", true, 1, CommandType.OTHER, ExecutionMode.CLIENT));
    this.collectionFilter = collectionFilter;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    String filter = payload.getInlineArg();
    Model[] elements = collectionFilter.filterContainsName(filter);
    String message = Arrays.toString(elements);
    return ExecutionResult.valueOf(true, message);
  }
}