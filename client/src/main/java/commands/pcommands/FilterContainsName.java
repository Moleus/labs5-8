package commands.pcommands;

import commands.*;
import model.CollectionFilter;
import model.data.Flat;

import java.util.Arrays;

public final class FilterContainsName extends AbstractCommand {
  private final CollectionFilter collectionFilter;

  public FilterContainsName(CollectionFilter collectionFilter) {
    super(CommandInfo.of("filter_contains_name", "Print elements with specified string in names", true, 1, false, ExecutionMode.CLIENT));
    this.collectionFilter = collectionFilter;
  }

  @Override
  public ExecutionResult execute(ExecutionPayload payload) {
    String filter = payload.getInlineArg();
    Flat[] elements = collectionFilter.filterContainsName(filter);
    String message = Arrays.toString(elements);
    return ExecutionResult.valueOf(true, message);
  }
}